package io.melody.core.activity.provider;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.jongo.Jongo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Component;

import com.github.pnavais.machine.StateMachine;
import com.github.pnavais.machine.api.message.Message;
import com.github.pnavais.machine.api.transition.TransitionIndex;
import com.github.pnavais.machine.importer.YAMLBufferImporter;
import com.github.pnavais.machine.model.State;
import com.github.pnavais.machine.model.StateTransition;
import com.github.pnavais.machine.model.StringMessage;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSortedMap;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.mongodb.client.gridfs.model.GridFSFile;

import io.melody.core.activity.StateKey;
import io.melody.core.activity.entity.ActivityEventEntity;
import io.melody.core.activity.repo.ActivityRepo;
import io.melody.core.enums.DbEnum;
import io.melody.core.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class ActivityProvider {
    @Autowired
    @Qualifier("jongoCore")
    private transient Jongo jongoCore;
    @Autowired
    @Qualifier("coreFS")
    private transient  GridFsTemplate coreFS;
    @Autowired
    private transient ActivityRepo activityRepo;

    @Value("${core-config.collections.core}")
    private String COLL_CORE_VALUES;

    private transient Gson gson;
    private Map<String, StateMachine> stFlows;
    private ImmutableMap.Builder<String, ImmutableSortedMap.Builder<StateKey, ActivityEventEntity>> immutableMap;

    @PostConstruct
    public void init() {
        gson = AppUtils.gsonInstance();
        this.stFlows = new HashMap<String, StateMachine>();
        this.immutableMap = ImmutableMap.builder();
 
        final List<org.json.simple.JSONObject> flows = this.obtainStateMachineList();
        flows.forEach(item -> {
            try {
                InputStream in = this.obtainStateMachine(item);
                StateMachine machine = YAMLBufferImporter.builder().build().parseFileBuffer(in);

                stFlows.put(StringUtils.toRootUpperCase(AppUtils.getAsStr(item, DbEnum.STF_FIELDS[1])), machine);

                ImmutableSortedMap.Builder<StateKey, ActivityEventEntity> eventsMap = ImmutableSortedMap
                        .orderedBy(java.util.Comparator.comparing(StateKey::getIdx));

                Map<State, Map<Message, State>> transitions = machine.getTransitionsIndex().getTransitionsAsMap();
                for (Map.Entry<State, Map<Message, State>> e : transitions.entrySet()) {
                    State srcState = e.getKey();

                    for (Map.Entry<Message, State> es : e.getValue().entrySet()) {
                        State targetState = es.getValue();
                        ActivityEventEntity event = new ActivityEventEntity(
                                StringUtils.toRootUpperCase(AppUtils.getAsStr(item, DbEnum.STF_FIELDS[1])));
                        event.setStatus(srcState.getName());
                        event.setEvent(es.getKey().toString());
                        event.setTarget(targetState.getName());
                        event.setAction(srcState.hasProperty(DbEnum.STF_FIELDS[2])
                                ? srcState.getProperty(DbEnum.STF_FIELDS[2]).get()
                                : null);
                        event.setFork(srcState.hasProperty(DbEnum.STF_FIELDS[3])
                                ? srcState.getProperty(DbEnum.STF_FIELDS[3]).get()
                                : null);
                        event.setSequenceNumber(
                                srcState.hasProperty(DbEnum.STF_FIELDS[4])
                                        ? NumberUtils.toInt(srcState.getProperty(DbEnum.STF_FIELDS[4]).get())
                                        : 0);
                        eventsMap.put(
                                new StateKey(e.getKey().getName(), srcState.getProperty(DbEnum.STF_FIELDS[4]).get()),
                                event);
                    }
                }
                immutableMap.put(StringUtils.toRootUpperCase(AppUtils.getAsStr(item, DbEnum.STF_FIELDS[1])), eventsMap);

            } catch (Exception e) {
                log.error(e.getMessage());
            }

        });

    }

    public Mono<ActivityEventEntity> obtainParentEvent(String email, String activityType) {
        List<ActivityEventEntity> activities = activityRepo.findParentByEmail(email,
                activityType);
        return Mono.justOrEmpty(!CollectionUtils.isEmpty(activities) ? activities.get(0) : null);
    }

    public Mono<ActivityEventEntity> obtainParentEvent(String email, String activityType, boolean flowIsDone) {
        List<ActivityEventEntity> activities = activityRepo.findParentByEmail(email,
                activityType, flowIsDone);
        return Mono.justOrEmpty(!CollectionUtils.isEmpty(activities) ? activities.get(0) : null);
    }

    public ActivityEventEntity obtainActiveEvent(String email, String activityType) {
        List<ActivityEventEntity> activities = activityRepo.findParentByEmail(email,
                activityType);
        return !CollectionUtils.isEmpty(activities) ? activities.get(0) : null;
    }

    public ActivityEventEntity obtainActiveEvent(String email, String deviceId, String activityType,
            boolean flowIsDone) {
        List<ActivityEventEntity> activities = activityRepo.findParentByDevice(email, deviceId,
                activityType, flowIsDone);
        return !CollectionUtils.isEmpty(activities) ? activities.get(0) : null;
    }

    public List<ActivityEventEntity> obtainActiveEvent(String email, String deviceId, String activityType) {
        return activityRepo.findParentByDevice(email, deviceId,
                activityType);
    }

    //
    public List<org.json.simple.JSONObject> obtainStateMachineList() {
        org.jongo.MongoCollection collection = jongoCore.getCollection(COLL_CORE_VALUES);
      org.json.simple.JSONObject value = collection.findOne(DbEnum.STF_CRITERIA_01, DbEnum.STF_CRITERIA_VALUE_01)
                .as(org.json.simple.JSONObject.class);

       return gson.fromJson(gson.toJson(value.get(DbEnum.STF_FIELDS[0])),
                new TypeToken<ArrayList<org.json.simple.JSONObject>>() {
               }.getType());
         
    }

    public InputStream obtainStateMachine(org.json.simple.JSONObject data) throws IllegalStateException, IOException {
        GridFSFile fsFile = coreFS.findOne(new Query(Criteria.where(DbEnum.STF_CRITERIA_02)
                .is(DbEnum.STF_CRITERIA_VALUE_02)
                .andOperator(
                        Criteria.where(DbEnum.STF_CRITERIA_03).is(AppUtils.getAsStr(data, DbEnum.STF_FIELDS[1])))));
        InputStream in = coreFS.getResource(fsFile).getInputStream();

        return in;
    }

    public Map<String, StateMachine> getStFlows() {
        return stFlows;
    }

    public StateMachine getFlow(String key) {
        return stFlows.get(key);
    }

    public TransitionIndex<State, Message, StateTransition> flowTransitionIndex(String key) {
        return this.getFlow(key).getTransitionsIndex();
    }

    public Optional<State> firstFlow(String key) {
        return this.flowTransitionIndex(key).getFirst();
    }

    public Optional<State> nextFlow(String key, String status, String event) {
        return this.flowTransitionIndex(key).getNext(new State(status), StringMessage.from(event));
    }

    public Optional<State> prevFlow(String key, String status, String event) {
        return this.flowTransitionIndex(key).getPrevious(new State(status), StringMessage.from(event));
    }

    public Optional<State> findState(String key, String status) {
        return this.flowTransitionIndex(key).find(status);
    }

    public List<StateTransition> flowTransition(String key, String status) {
        return this.flowTransitionIndex(key).getTransitions(status).stream().collect(Collectors.toList());
    }

    public Optional<ActivityEventEntity> firstEvent(String key) {
        return Optional.ofNullable(this.immutableMap.build().get(key).build().firstEntry().getValue());
    }

    public Optional<ActivityEventEntity> findEvent(String key, String status) {
        Optional<State> state = this.findState(key, status);
        final String idx = (state.get().hasProperty(DbEnum.STF_FIELDS[4]))
                ? state.get().getProperty(DbEnum.STF_FIELDS[4]).get()
                : String.valueOf(0);
        return Optional.ofNullable(this.immutableMap.build().get(key).build().get(new StateKey(status, idx)));
    }

    public Optional<ActivityEventEntity> prevEvent(String key, String status) {
        Optional<State> state = this.findState(key, status);
        final String idx = (state.get().hasProperty(DbEnum.STF_FIELDS[4]))
                ? state.get().getProperty(DbEnum.STF_FIELDS[4]).get()
                : String.valueOf(0);
        Optional<Map.Entry<StateKey, ActivityEventEntity>> event = Optional
                .ofNullable(this.immutableMap.build().get(key).build().lowerEntry(new StateKey(status, idx)));

        return event.isPresent() ? Optional.of(event.get().getValue()) : Optional.empty();
    }

    public Optional<ActivityEventEntity> nextEvent(String key, String status) {
        Optional<State> state = this.findState(key, status);
        final String idx = (state.get().hasProperty(DbEnum.STF_FIELDS[4]))
                ? state.get().getProperty(DbEnum.STF_FIELDS[4]).get()
                : String.valueOf(0);
        Optional<Map.Entry<StateKey, ActivityEventEntity>> event = Optional
                .ofNullable(this.immutableMap.build().get(key).build().higherEntry(new StateKey(status, idx)));

        return event.isPresent() ? Optional.of(event.get().getValue()) : Optional.empty();
    }

    public ImmutableSortedMap.Builder<StateKey, ActivityEventEntity> getFlowNavigate(String key) {

        return immutableMap.build().get(key);
    }
}
