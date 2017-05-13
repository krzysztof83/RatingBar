package models;

import android.support.annotation.NonNull;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by RENT on 2017-05-10.
 */
@Getter
@Setter
public class Person {

    public static final String  TABLE_NAME= "people";

    public static final String ID = "_id";

    public static final String NAME = "name";//@NonNull

    public static final String RATING = "rating";

    public static final String PHONE = "phone";//@NonNull

    private Long id;
    private String name;
    private String phone;
    private double rating;
}
