package com.linyeah.erp;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by k on 5/28/15.
 */
public class DateAdapter {

    private final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

    public String marshal(final Date object) throws Exception {
        return this.dateFormat.format(object);
    }

}
