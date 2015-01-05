package cz.ondraster.bettersleeping.logic;

public class MinecraftTime {
   public int hour;
   public int minute;

   public static MinecraftTime getFromWorldTime(long time) {
      MinecraftTime ret = new MinecraftTime();
      long localTime = time % 24000;
      ret.hour = (int) (localTime / 1000);
      ret.minute = (int) ((double) (localTime % 1000) / 20 * (60 / 50));

      return ret;
   }

   public static MinecraftTime getFromTime(int hour, int minute) {
      MinecraftTime ret = new MinecraftTime();
      ret.hour = hour;
      ret.minute = minute;
      return ret;
   }

   @Override
   public String toString() {
      int localHr = hour + 6;
      localHr %= 24;

      return (localHr < 10 ? "0" : "") + localHr + ":" + (minute < 10 ? "0" : "") + minute;
   }

   public int getRealHour() {
      return (hour + 6) % 24;
   }

   public int getRealMinute() {
      return minute;
   }

   public static long extrapolateTime(int hour, int minute) {
      minute = (int) (minute * (double) (50 / 60));
      return 20 * minute + (hour * 50 * 20);
   }
}
