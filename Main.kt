const val DEFAULT_N_OF_SPOTS = 10

interface Vehicle {
    fun getRegNumber(): String
    fun getColor(): String
}

class Car(private val regNumber: String, private val color: String): Vehicle {

    override fun getRegNumber() = regNumber
    override fun getColor() = color
}

class ParkingArea(private var totalSpots: Int = DEFAULT_N_OF_SPOTS) {

    private val vehiclesParked = mutableListOf<Vehicle?>()
    private var availableSpots = totalSpots

    init {
        if (totalSpots < 1) {
            totalSpots = DEFAULT_N_OF_SPOTS
            availableSpots = DEFAULT_N_OF_SPOTS
        }
        println("Created a parking lot with $totalSpots spots.")
    }

    fun parkVehicle(vehicle: Vehicle) {
        if (availableSpots > 0) {
            availableSpots--
            var spot = -1
            for (i in vehiclesParked.indices) {
                if (vehiclesParked[i] == null) {
                    vehiclesParked[i] = vehicle
                    spot = i
                    break
                }
            }
            if (spot == -1) vehiclesParked.add(vehicle)
            println("${vehicle.getColor()} car parked in spot ${if (spot == -1) vehiclesParked.size else spot + 1}.")
        } else {
            println("Sorry, the parking lot is full.")
        }
    }

    fun releaseVehicle(spot: Int) {
        if (spot - 1 !in vehiclesParked.indices ||
            (spot - 1 in vehiclesParked.indices && vehiclesParked[spot - 1] == null)) {
            println("There is no car in spot $spot.")
        } else {
            vehiclesParked[spot - 1] = null
            availableSpots++
            println("Spot $spot is free.")
        }
    }

    fun printStatus() {
        var allNullValues = true
        for (i in vehiclesParked.indices) {
            if (vehiclesParked[i] != null) {
                println("${i + 1} ${vehiclesParked[i]!!.getRegNumber()} ${vehiclesParked[i]!!.getColor()}")
                allNullValues = false
            }
        }
        if (vehiclesParked.isEmpty() || allNullValues) {
            println("Parking lot is empty.")
        }
    }

    fun printRegistrationNumbersByColor(color: String) {
        var carsWithThisColor = false
        for (vehicle in vehiclesParked) {
            if (vehicle != null && vehicle.getColor().lowercase() == color.lowercase()) {
                if (carsWithThisColor) print(", ")
                print(vehicle.getRegNumber())
                carsWithThisColor = true
            }
        }
        if (carsWithThisColor) println()
        else println("No cars with color $color were found.")
    }

    fun printSpotsByColor(color: String) {
        var carsWithThisColor = false
        for (i in vehiclesParked.indices) {
            if (vehiclesParked[i] != null && vehiclesParked[i]!!.getColor().lowercase() == color.lowercase()) {
                if (carsWithThisColor) print(", ")
                print(i + 1)
                carsWithThisColor = true
            }
        }
        if (carsWithThisColor) println()
        else println("No cars with color $color were found.")
    }

    fun printSpotByRegistrationNumber(regNumber: String) {
        for (i in vehiclesParked.indices) {
            if (vehiclesParked[i] != null && vehiclesParked[i]!!.getRegNumber() == regNumber) {
                println(i + 1)
                return
            }
        }
        println("No cars with registration number $regNumber were found.")
    }
}

fun parkingAreaNotCreated() {
    println("Sorry, a parking lot has not been created.")
}

fun main() {
    var parkingArea: ParkingArea? = null

    while (true) {
        val input = readln().split(" ")
        when(input.size) {
            1 -> {
                when (input[0]) {
                    "exit"   -> break
                    "status" -> parkingArea?.printStatus() ?: parkingAreaNotCreated()
                    else     -> println("Invalid input")
                }
            }
            2 -> {
                when (input[0]) {
                    "leave" -> {
                        if (input[1].matches("\\d+".toRegex())) {
                            parkingArea?.releaseVehicle(input[1].toInt()) ?: parkingAreaNotCreated()
                        } else println("${input[1]} is invalid")
                    }
                    "create" -> {
                        if (input[1].matches("\\d+".toRegex())) {
                            parkingArea = ParkingArea(input[1].toInt())
                        } else println("${input[1]} is invalid")
                    }
                    "reg_by_color"  -> parkingArea?.printRegistrationNumbersByColor(input[1]) ?: parkingAreaNotCreated()
                    "spot_by_color" -> parkingArea?.printSpotsByColor(input[1])               ?: parkingAreaNotCreated()
                    "spot_by_reg"   -> parkingArea?.printSpotByRegistrationNumber(input[1])   ?: parkingAreaNotCreated()
                    else            -> println("Invalid input")
                }
            }
            3 -> {
                if (input[0] == "park") {
                    parkingArea?.parkVehicle(Car(input[1], input[2])) ?: parkingAreaNotCreated()
                } else println("Invalid input")
            }
        }
    }
}
