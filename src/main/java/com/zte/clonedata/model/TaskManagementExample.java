package com.zte.clonedata.model;

import java.util.ArrayList;
import java.util.List;

public class TaskManagementExample {
    protected String orderByClause;

    protected boolean distinct;

    protected List<Criteria> oredCriteria;

    public TaskManagementExample() {
        oredCriteria = new ArrayList<Criteria>();
    }

    public void setOrderByClause(String orderByClause) {
        this.orderByClause = orderByClause;
    }

    public String getOrderByClause() {
        return orderByClause;
    }

    public void setDistinct(boolean distinct) {
        this.distinct = distinct;
    }

    public boolean isDistinct() {
        return distinct;
    }

    public List<Criteria> getOredCriteria() {
        return oredCriteria;
    }

    public void or(Criteria criteria) {
        oredCriteria.add(criteria);
    }

    public Criteria or() {
        Criteria criteria = createCriteriaInternal();
        oredCriteria.add(criteria);
        return criteria;
    }

    public Criteria createCriteria() {
        Criteria criteria = createCriteriaInternal();
        if (oredCriteria.size() == 0) {
            oredCriteria.add(criteria);
        }
        return criteria;
    }

    protected Criteria createCriteriaInternal() {
        Criteria criteria = new Criteria();
        return criteria;
    }

    public void clear() {
        oredCriteria.clear();
        orderByClause = null;
        distinct = false;
    }

    protected abstract static class GeneratedCriteria {
        protected List<Criterion> criteria;

        protected GeneratedCriteria() {
            super();
            criteria = new ArrayList<Criterion>();
        }

        public boolean isValid() {
            return criteria.size() > 0;
        }

        public List<Criterion> getAllCriteria() {
            return criteria;
        }

        public List<Criterion> getCriteria() {
            return criteria;
        }

        protected void addCriterion(String condition) {
            if (condition == null) {
                throw new RuntimeException("Value for condition cannot be null");
            }
            criteria.add(new Criterion(condition));
        }

        protected void addCriterion(String condition, Object value, String property) {
            if (value == null) {
                throw new RuntimeException("Value for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value));
        }

        protected void addCriterion(String condition, Object value1, Object value2, String property) {
            if (value1 == null || value2 == null) {
                throw new RuntimeException("Between values for " + property + " cannot be null");
            }
            criteria.add(new Criterion(condition, value1, value2));
        }

        public Criteria andIdIsNull() {
            addCriterion("id is null");
            return (Criteria) this;
        }

        public Criteria andIdIsNotNull() {
            addCriterion("id is not null");
            return (Criteria) this;
        }

        public Criteria andIdEqualTo(String value) {
            addCriterion("id =", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotEqualTo(String value) {
            addCriterion("id <>", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThan(String value) {
            addCriterion("id >", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdGreaterThanOrEqualTo(String value) {
            addCriterion("id >=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThan(String value) {
            addCriterion("id <", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLessThanOrEqualTo(String value) {
            addCriterion("id <=", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdLike(String value) {
            addCriterion("id like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotLike(String value) {
            addCriterion("id not like", value, "id");
            return (Criteria) this;
        }

        public Criteria andIdIn(List<String> values) {
            addCriterion("id in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotIn(List<String> values) {
            addCriterion("id not in", values, "id");
            return (Criteria) this;
        }

        public Criteria andIdBetween(String value1, String value2) {
            addCriterion("id between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andIdNotBetween(String value1, String value2) {
            addCriterion("id not between", value1, value2, "id");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetIsNull() {
            addCriterion("excute_target is null");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetIsNotNull() {
            addCriterion("excute_target is not null");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetEqualTo(String value) {
            addCriterion("excute_target =", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetNotEqualTo(String value) {
            addCriterion("excute_target <>", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetGreaterThan(String value) {
            addCriterion("excute_target >", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetGreaterThanOrEqualTo(String value) {
            addCriterion("excute_target >=", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetLessThan(String value) {
            addCriterion("excute_target <", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetLessThanOrEqualTo(String value) {
            addCriterion("excute_target <=", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetLike(String value) {
            addCriterion("excute_target like", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetNotLike(String value) {
            addCriterion("excute_target not like", value, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetIn(List<String> values) {
            addCriterion("excute_target in", values, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetNotIn(List<String> values) {
            addCriterion("excute_target not in", values, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetBetween(String value1, String value2) {
            addCriterion("excute_target between", value1, value2, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExcuteTargetNotBetween(String value1, String value2) {
            addCriterion("excute_target not between", value1, value2, "excuteTarget");
            return (Criteria) this;
        }

        public Criteria andExternalCodeIsNull() {
            addCriterion("external_code is null");
            return (Criteria) this;
        }

        public Criteria andExternalCodeIsNotNull() {
            addCriterion("external_code is not null");
            return (Criteria) this;
        }

        public Criteria andExternalCodeEqualTo(String value) {
            addCriterion("external_code =", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeNotEqualTo(String value) {
            addCriterion("external_code <>", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeGreaterThan(String value) {
            addCriterion("external_code >", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeGreaterThanOrEqualTo(String value) {
            addCriterion("external_code >=", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeLessThan(String value) {
            addCriterion("external_code <", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeLessThanOrEqualTo(String value) {
            addCriterion("external_code <=", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeLike(String value) {
            addCriterion("external_code like", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeNotLike(String value) {
            addCriterion("external_code not like", value, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeIn(List<String> values) {
            addCriterion("external_code in", values, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeNotIn(List<String> values) {
            addCriterion("external_code not in", values, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeBetween(String value1, String value2) {
            addCriterion("external_code between", value1, value2, "externalCode");
            return (Criteria) this;
        }

        public Criteria andExternalCodeNotBetween(String value1, String value2) {
            addCriterion("external_code not between", value1, value2, "externalCode");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanIsNull() {
            addCriterion("task_excute_plan is null");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanIsNotNull() {
            addCriterion("task_excute_plan is not null");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanEqualTo(String value) {
            addCriterion("task_excute_plan =", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanNotEqualTo(String value) {
            addCriterion("task_excute_plan <>", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanGreaterThan(String value) {
            addCriterion("task_excute_plan >", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanGreaterThanOrEqualTo(String value) {
            addCriterion("task_excute_plan >=", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanLessThan(String value) {
            addCriterion("task_excute_plan <", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanLessThanOrEqualTo(String value) {
            addCriterion("task_excute_plan <=", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanLike(String value) {
            addCriterion("task_excute_plan like", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanNotLike(String value) {
            addCriterion("task_excute_plan not like", value, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanIn(List<String> values) {
            addCriterion("task_excute_plan in", values, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanNotIn(List<String> values) {
            addCriterion("task_excute_plan not in", values, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanBetween(String value1, String value2) {
            addCriterion("task_excute_plan between", value1, value2, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskExcutePlanNotBetween(String value1, String value2) {
            addCriterion("task_excute_plan not between", value1, value2, "taskExcutePlan");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNull() {
            addCriterion("task_id is null");
            return (Criteria) this;
        }

        public Criteria andTaskIdIsNotNull() {
            addCriterion("task_id is not null");
            return (Criteria) this;
        }

        public Criteria andTaskIdEqualTo(String value) {
            addCriterion("task_id =", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotEqualTo(String value) {
            addCriterion("task_id <>", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThan(String value) {
            addCriterion("task_id >", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdGreaterThanOrEqualTo(String value) {
            addCriterion("task_id >=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThan(String value) {
            addCriterion("task_id <", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLessThanOrEqualTo(String value) {
            addCriterion("task_id <=", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdLike(String value) {
            addCriterion("task_id like", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotLike(String value) {
            addCriterion("task_id not like", value, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdIn(List<String> values) {
            addCriterion("task_id in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotIn(List<String> values) {
            addCriterion("task_id not in", values, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdBetween(String value1, String value2) {
            addCriterion("task_id between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskIdNotBetween(String value1, String value2) {
            addCriterion("task_id not between", value1, value2, "taskId");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNull() {
            addCriterion("task_name is null");
            return (Criteria) this;
        }

        public Criteria andTaskNameIsNotNull() {
            addCriterion("task_name is not null");
            return (Criteria) this;
        }

        public Criteria andTaskNameEqualTo(String value) {
            addCriterion("task_name =", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotEqualTo(String value) {
            addCriterion("task_name <>", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThan(String value) {
            addCriterion("task_name >", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameGreaterThanOrEqualTo(String value) {
            addCriterion("task_name >=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThan(String value) {
            addCriterion("task_name <", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLessThanOrEqualTo(String value) {
            addCriterion("task_name <=", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameLike(String value) {
            addCriterion("task_name like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotLike(String value) {
            addCriterion("task_name not like", value, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameIn(List<String> values) {
            addCriterion("task_name in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotIn(List<String> values) {
            addCriterion("task_name not in", values, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameBetween(String value1, String value2) {
            addCriterion("task_name between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskNameNotBetween(String value1, String value2) {
            addCriterion("task_name not between", value1, value2, "taskName");
            return (Criteria) this;
        }

        public Criteria andTaskStatusIsNull() {
            addCriterion("task_status is null");
            return (Criteria) this;
        }

        public Criteria andTaskStatusIsNotNull() {
            addCriterion("task_status is not null");
            return (Criteria) this;
        }

        public Criteria andTaskStatusEqualTo(String value) {
            addCriterion("task_status =", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusNotEqualTo(String value) {
            addCriterion("task_status <>", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusGreaterThan(String value) {
            addCriterion("task_status >", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusGreaterThanOrEqualTo(String value) {
            addCriterion("task_status >=", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusLessThan(String value) {
            addCriterion("task_status <", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusLessThanOrEqualTo(String value) {
            addCriterion("task_status <=", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusLike(String value) {
            addCriterion("task_status like", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusNotLike(String value) {
            addCriterion("task_status not like", value, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusIn(List<String> values) {
            addCriterion("task_status in", values, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusNotIn(List<String> values) {
            addCriterion("task_status not in", values, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusBetween(String value1, String value2) {
            addCriterion("task_status between", value1, value2, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskStatusNotBetween(String value1, String value2) {
            addCriterion("task_status not between", value1, value2, "taskStatus");
            return (Criteria) this;
        }

        public Criteria andTaskTypeIsNull() {
            addCriterion("task_type is null");
            return (Criteria) this;
        }

        public Criteria andTaskTypeIsNotNull() {
            addCriterion("task_type is not null");
            return (Criteria) this;
        }

        public Criteria andTaskTypeEqualTo(String value) {
            addCriterion("task_type =", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotEqualTo(String value) {
            addCriterion("task_type <>", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeGreaterThan(String value) {
            addCriterion("task_type >", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeGreaterThanOrEqualTo(String value) {
            addCriterion("task_type >=", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeLessThan(String value) {
            addCriterion("task_type <", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeLessThanOrEqualTo(String value) {
            addCriterion("task_type <=", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeLike(String value) {
            addCriterion("task_type like", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotLike(String value) {
            addCriterion("task_type not like", value, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeIn(List<String> values) {
            addCriterion("task_type in", values, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotIn(List<String> values) {
            addCriterion("task_type not in", values, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeBetween(String value1, String value2) {
            addCriterion("task_type between", value1, value2, "taskType");
            return (Criteria) this;
        }

        public Criteria andTaskTypeNotBetween(String value1, String value2) {
            addCriterion("task_type not between", value1, value2, "taskType");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondIsNull() {
            addCriterion("timeout_second is null");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondIsNotNull() {
            addCriterion("timeout_second is not null");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondEqualTo(Short value) {
            addCriterion("timeout_second =", value, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondNotEqualTo(Short value) {
            addCriterion("timeout_second <>", value, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondGreaterThan(Short value) {
            addCriterion("timeout_second >", value, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondGreaterThanOrEqualTo(Short value) {
            addCriterion("timeout_second >=", value, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondLessThan(Short value) {
            addCriterion("timeout_second <", value, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondLessThanOrEqualTo(Short value) {
            addCriterion("timeout_second <=", value, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondIn(List<Short> values) {
            addCriterion("timeout_second in", values, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondNotIn(List<Short> values) {
            addCriterion("timeout_second not in", values, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondBetween(Short value1, Short value2) {
            addCriterion("timeout_second between", value1, value2, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andTimeoutSecondNotBetween(Short value1, Short value2) {
            addCriterion("timeout_second not between", value1, value2, "timeoutSecond");
            return (Criteria) this;
        }

        public Criteria andCreDtIsNull() {
            addCriterion("cre_dt is null");
            return (Criteria) this;
        }

        public Criteria andCreDtIsNotNull() {
            addCriterion("cre_dt is not null");
            return (Criteria) this;
        }

        public Criteria andCreDtEqualTo(String value) {
            addCriterion("cre_dt =", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtNotEqualTo(String value) {
            addCriterion("cre_dt <>", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtGreaterThan(String value) {
            addCriterion("cre_dt >", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtGreaterThanOrEqualTo(String value) {
            addCriterion("cre_dt >=", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtLessThan(String value) {
            addCriterion("cre_dt <", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtLessThanOrEqualTo(String value) {
            addCriterion("cre_dt <=", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtLike(String value) {
            addCriterion("cre_dt like", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtNotLike(String value) {
            addCriterion("cre_dt not like", value, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtIn(List<String> values) {
            addCriterion("cre_dt in", values, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtNotIn(List<String> values) {
            addCriterion("cre_dt not in", values, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtBetween(String value1, String value2) {
            addCriterion("cre_dt between", value1, value2, "creDt");
            return (Criteria) this;
        }

        public Criteria andCreDtNotBetween(String value1, String value2) {
            addCriterion("cre_dt not between", value1, value2, "creDt");
            return (Criteria) this;
        }
    }

    public static class Criteria extends GeneratedCriteria {

        protected Criteria() {
            super();
        }
    }

    public static class Criterion {
        private String condition;

        private Object value;

        private Object secondValue;

        private boolean noValue;

        private boolean singleValue;

        private boolean betweenValue;

        private boolean listValue;

        private String typeHandler;

        public String getCondition() {
            return condition;
        }

        public Object getValue() {
            return value;
        }

        public Object getSecondValue() {
            return secondValue;
        }

        public boolean isNoValue() {
            return noValue;
        }

        public boolean isSingleValue() {
            return singleValue;
        }

        public boolean isBetweenValue() {
            return betweenValue;
        }

        public boolean isListValue() {
            return listValue;
        }

        public String getTypeHandler() {
            return typeHandler;
        }

        protected Criterion(String condition) {
            super();
            this.condition = condition;
            this.typeHandler = null;
            this.noValue = true;
        }

        protected Criterion(String condition, Object value, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.typeHandler = typeHandler;
            if (value instanceof List<?>) {
                this.listValue = true;
            } else {
                this.singleValue = true;
            }
        }

        protected Criterion(String condition, Object value) {
            this(condition, value, null);
        }

        protected Criterion(String condition, Object value, Object secondValue, String typeHandler) {
            super();
            this.condition = condition;
            this.value = value;
            this.secondValue = secondValue;
            this.typeHandler = typeHandler;
            this.betweenValue = true;
        }

        protected Criterion(String condition, Object value, Object secondValue) {
            this(condition, value, secondValue, null);
        }
    }
}