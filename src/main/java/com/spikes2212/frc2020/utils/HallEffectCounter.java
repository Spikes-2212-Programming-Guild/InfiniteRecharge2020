package com.spikes2212.frc2020.utils;

import edu.wpi.first.wpilibj.DigitalInput;

public class HallEffectCounter {
  private DigitalInput hallEffect;
  private int counter = 0;
  private boolean lastOn = false;

  public HallEffectCounter(DigitalInput hallEffect) {
    this.hallEffect = hallEffect;
  }

  public void update(double speed) {
    boolean isOn = hallEffect.get();
    if (isOn && !lastOn) {
      if (speed > 0)
        counter++;
      else
        counter--;
    }

    lastOn = isOn;
  }

  public int getCurrentMagnet() {
    return counter;
  }

  public boolean atTop(int numOfMagnets) {
    return numOfMagnets - 1 == getCurrentMagnet();
  }
}
