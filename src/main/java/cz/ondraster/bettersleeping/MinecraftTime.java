package cz.ondraster.bettersleeping;

public class MinecraftTime {
   public int hour;
   public int minute;

   public static MinecraftTime getFromWorldTime(long time) {
      MinecraftTime ret = new MinecraftTime();
      long localTime = time % 24000;
      ret.hour = (int) (localTime / 1000);
      ret.minute = (int) (localTime % 1000) / 20;

      return ret;
   }
}
