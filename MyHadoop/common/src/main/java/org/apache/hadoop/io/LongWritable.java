package org.apache.hadoop.io;

import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;

public class LongWritable implements Writable{

  private long value;

  public LongWritable() {}

  public LongWritable(long value) { set(value); }

  /** Set the value of this LongWritable. */
  public void set(long value) { this.value = value; }

  /** Return the value of this LongWritable. */
  public long get() { return value; }

  public void readFields(DataInput in) throws IOException {
    value = in.readLong();
  }

  public void write(DataOutput out) throws IOException {
    out.writeLong(value);
  }
}
