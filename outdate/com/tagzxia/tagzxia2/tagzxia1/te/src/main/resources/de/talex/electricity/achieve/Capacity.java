package com.tagzxia2.tagzxia1.te.src.main.resources.de.talex.electricity.achieve;

import lombok.Getter;
import lombok.Setter;
import lombok.SneakyThrows;
import pubsher.talexsoultech.talex.electricity.exception.CapacityVoltageCannotMatch;

/**
 * 电力实例
 */
@Getter
@Setter
public class Capacity {

    /**
     * 已存储电量
     **/
    private double storageCapacity;

    /**
     * 电压大小
     **/
    private double voltage;

    /**
     * 实例化一个电力电量
     *
     * @param storageCapacity 电量数量
     * @param voltage         电量电压
     */
    public Capacity(double storageCapacity, double voltage) {

        this.storageCapacity = storageCapacity;
        this.voltage = voltage;

    }

    private boolean voltageVerify(double voltage) {

        if ( this.voltage <= 36 && voltage > 36 ) {

            if ( this.voltage <= 330 && voltage > 330 ) {

                if ( this.voltage <= 10000 && voltage > 10000 ) {

                    return this.voltage <= 1000000 && voltage > 1000000;

                }

            }

        }

        return true;

    }


    @SneakyThrows
    public Capacity addStorageCapacity(Capacity capacity) {

        if ( !voltageVerify(capacity.voltage) ) {
            throw new CapacityVoltageCannotMatch();
        }

        this.storageCapacity += capacity.storageCapacity;

        capacity.storageCapacity = 0;

        return this;

    }

    @SneakyThrows
    public Capacity provideStorageCapacity(Capacity capacity) {

        if ( !voltageVerify(capacity.voltage) ) {
            throw new CapacityVoltageCannotMatch();
        }

        if ( this.storageCapacity <= 0 ) {
            return null;
        }

        if ( storageCapacity >= capacity.storageCapacity ) {

            double a = capacity.storageCapacity;
            capacity.storageCapacity = 0;
            this.storageCapacity -= a;

            return new Capacity(a, voltage);

        } else {

            double a = this.storageCapacity;
            capacity.storageCapacity = 0;
            capacity.storageCapacity -= a;

            return new Capacity(a, voltage);

        }

    }

    @SneakyThrows
    public boolean canProvideCapacity(Capacity willCapacity) {

        if ( !voltageVerify(willCapacity.voltage) ) {
            throw new CapacityVoltageCannotMatch();
        }

        return storageCapacity > 0;

    }

}
