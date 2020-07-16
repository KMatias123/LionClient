package xaero.common.api.spigot.message;

import xaero.common.api.spigot.ServerWaypoint;

public abstract class MessageWaypoint implements Runnable {
  protected char packetID;
  
  protected String worldUID;
  
  protected String worldName;
  
  protected ServerWaypoint waypoint;
  
  protected int waypointID;
  
  public abstract void run();
}


/* Location:              C:\Users\ioanm\Downloads\Xaeros_Minimap_20.15.3_Forge_1.12.jar!\xaero\common\api\spigot\message\MessageWaypoint.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */