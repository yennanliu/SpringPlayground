package com.yen.data.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.lang.reflect.Array;
import java.util.ArrayList;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class History {

    private ArrayList<Task> tasks;
}
