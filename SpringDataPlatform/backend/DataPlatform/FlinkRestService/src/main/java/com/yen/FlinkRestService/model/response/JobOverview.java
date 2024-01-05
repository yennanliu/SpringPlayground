package com.yen.FlinkRestService.model.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashMap;

/**
 *   {
 *     jid: "02450c7ce9fce955c3192f49c56d5d2d",
 *     name: "Flink Streaming Job",
 *     start-time: 1704432045790,
 *     end-time: 1704432046445,
 *     duration: 655,
 *     state: "FINISHED",
 *     last-modification: 1704432046445,
 *     tasks: {
 *      running: 0,
 *      canceling: 0,
 *      canceled: 0,
 *      total: 3,
 *      created: 0,
 *      scheduled: 0,
 *      deploying: 0,
 *      reconciling: 0,
 *      finished: 3,
 *      initializing: 0,
 *      failed: 0
 *      }
 *  }
 *
 *
 */

@AllArgsConstructor
@NoArgsConstructor
@Data
@ToString
public class JobOverview {

    private String jid;
    private String name;
    private long startTime;
    private long endTime;
    private int duration;
    private String state;
    private long lastModification;
    private HashMap<String, Object> tasks;
}
