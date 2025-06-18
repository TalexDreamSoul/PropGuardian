package me.arasple.mc.trhologram.api

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.World
import kotlin.math.pow
import kotlin.math.sqrt

/**
 * @author Arasple
 * @date 2021/2/10 10:53
 */
class Position: Cloneable {

    val world: World
    val x: Double
    val y: Double
    val z: Double

    constructor(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) : this(Bukkit.getWorlds()[0], x, y, z)

    constructor(world: World = Bukkit.getWorlds()[0], x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) {
        this.world = world
        this.x = x
        this.y = y
        this.z = z
    }

    fun distance(l: Position) =
        distance(l.x, l.y, l.z)

    fun distance(l: Location) =
        distance(l.x, l.y, l.z)

    fun clone(x: Double = 0.0, y: Double = 0.0, z: Double = 0.0) =
        Position(world, this.x + x, this.y + y, this.z + z)

    private fun distance(x: Double, y: Double, z: Double) =
        sqrt((this.x - x).pow(2) + (this.y - y).pow(2) + (this.z - z).pow(2))

    fun toVelocity() =
        InstantVector(x, y, z)

    fun toLocation() =
        Location(world, x, y, z)

    override fun toString() =
        "${world.name} ~ $x, $y, $z"

    override fun clone(): Location {
        return super.clone() as Location
    }

    companion object {

        @JvmStatic
        fun fromLocation(l: Location) =
            Position(l.world!!, l.x, l.y, l.z)

    }

}