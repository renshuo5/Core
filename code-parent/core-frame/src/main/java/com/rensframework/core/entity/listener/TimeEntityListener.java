package com.rensframework.core.entity.listener;

import java.sql.Timestamp;

import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;

import com.rensframework.core.entity.TimeEntity;

public class TimeEntityListener {
	  @PrePersist
	  public void onPrePersist(TimeEntity e)
	  {
	    Timestamp t = new Timestamp(System.currentTimeMillis());
	    e.setCreation(t);
	    e.setLastModified(t);
	  }
	  
	  @PreUpdate
	  public void onPreUpdate(TimeEntity e)
	  {
	    e.setLastModified(new Timestamp(System.currentTimeMillis()));
	  }
}
