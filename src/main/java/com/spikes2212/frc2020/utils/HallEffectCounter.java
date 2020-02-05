package com.spikes2212.frc2020.utils;

import edu.wpi.first.wpilibj.DigitalInput;

public class HallEffectCounter {
    private DigitalInput hallEffect;
    private int counter;
    private boolean lastIndex;

    public HallEffectCounter(DigitalInput hallEffect) {
        this.hallEffect = hallEffect;
        counter = 0;
        lastIndex = false;
    }

    public void update(double speed) {
        if (!hallEffect.get())
            lastIndex = false;
        else {
            if (!lastIndex) {
                if (speed > 0)
                    counter++;
                else
                    counter--;
                lastIndex = true;
            }
        }
    }

    public int getCounter() {
        return counter;
    }
}
