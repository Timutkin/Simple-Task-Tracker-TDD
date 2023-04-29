package ru.timutkin.tdd.web.constant;


import lombok.experimental.UtilityClass;

@UtilityClass
public class ValidationConstant {

    public static final String TASK_WITH_ID_NOT_FOUND = "Task with id = %d not found";
    public static final String USER_WITH_ID_NOT_FOUND = "User with id = %d not found";

    public static final String THE_PROJECT_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0 = "The project id should not be <= 0";
    public static final String PROJECT_WITH_ID_NOT_FOUND = "Project with id = %d not found";
    public static final String CORRECT_DATA_FORMAT = "Correct format: yyyy-MM-dd HH:mm";
    public static final String VALUES_OPEN_IN_PROGRESS_RESOLVED_REOPENED_CLOSED = "Acceptable values: OPEN, IN_PROGRESS, RESOLVED, REOPENED, CLOSED";
    public static final String THE_TASK_NAME_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES = "The task name should not be empty, consist of spaces";

    public static final String THE_ID_SHOULD_NOT_BE_NULL_OR_LESS_OR_EQUAL_0 = "The id should not be null or <= 0";
    public static final String THE_MESSAGE_SHOULD_NOT_BE_EMPTY_CONSIST_OF_SPACES = "The message should not be empty, consist of spaces";
    public static final String THE_USER_ID_SHOULD_NOT_BE_LESS_OR_EQUAL_0 = "The user id should not be <= 0";

}
