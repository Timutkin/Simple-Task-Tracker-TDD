package ru.timutkin.tdd.web.constant;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SwaggerDescription {

    public static final String CREATE_TASK = "Each task must necessarily contain a title, description (message) that is not empty or does not consist of spaces. " +
                                             "The task is assigned to the user, therefore its identifier must be specified. " +
                                             "Also, the task corresponds to the project, so its ID must also be specified.";
    public static final String UPDATE_TASK = "The method accepts an object of the TaskDTO class, which must contain the task identifier, as well as valid data: " +
                                             "not empty sequences for text fields, identifiers larger than zero, they must also be in the system," +
                                             "data format : uuuu-MM-dd'T'HH:mm";

}
