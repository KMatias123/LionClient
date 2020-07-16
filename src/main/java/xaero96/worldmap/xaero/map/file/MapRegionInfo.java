package xaero.map.file;

import java.io.File;

public interface MapRegionInfo {
  boolean shouldCache();
  
  File getRegionFile();
  
  File getCacheFile();
  
  String getWorld();
  
  int getRegionX();
  
  int getRegionZ();
  
  void setShouldCache(boolean paramBoolean, String paramString);
  
  void setCacheFile(File paramFile);
}


/* Location:              C:\Users\ioanm\Downloads\XaerosWorldMap_1.7.3_Forge_1.12.jar!\xaero\map\file\MapRegionInfo.class
 * Java compiler version: 7 (51.0)
 * JD-Core Version:       1.1.3
 */