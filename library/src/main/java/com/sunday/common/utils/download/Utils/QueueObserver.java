package com.sunday.common.utils.download.Utils;

/**
 * Created by Majid Golshadi on 4/22/2014.
 */
public interface QueueObserver {

    void wakeUp(int taskID);
}
