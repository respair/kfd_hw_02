import kotlin.system.exitProcess

sealed interface Transport {
    val maxSpeed: Int
    val passengers: Int
    val name: String
    val number: String

    fun price(km : Float, price: Int, type: String, psg: Int) : Float {
        val i : Float = when (type) {
            "economy" -> 0.5F
            "business" -> 2F
            "luxury" -> 4F
            else -> 0F
        }
        return price * km * 10 / psg + 1000 * i
    }
}

open class Car(override var name: String = "", override var number: String = "") : Transport {
    override var maxSpeed: Int = 100
    override var passengers: Int = 4

    val liters: Int = 20

    fun refillPrice(price: Float, liters: Int): Float {
        return price * liters
    }
    open fun parkingPrice(price : Float, hour : Float) : Float {
        return price * hour
    }
}

class MotorBike(override var name: String = "", override var number: String = "") : Car(name, number) {
    override fun parkingPrice(price : Float, hour : Float) : Float {
        return (price / 2) * hour
    }
    fun price(km : Float, price: Int) : Float {
        return price * km + 1000
    }
}

class Train(override var name: String = "", override var number: String = "") : Transport {
    override var maxSpeed: Int = 65
    override var passengers: Int = 1000
    private var carriages: Int = 10

    fun energy(): Int {
        val num : Int = carriages/2
        return num * 300 // 1 trip (Kw)
    }

    fun price(km: Float) : Int {
        return energy() / passengers + 100 + (km / 10).toInt()
    }
}

class Airplane(override var name: String = "", override var number: String = "") : Car(name, number) {
    override fun price(km : Float, price: Int, type: String, psg: Int) : Float {
        val i : Float = when (type) {
            "economy" -> 1F
            "business" -> 4F
            "luxury" -> 6F
            else -> 0F
        }
        return price * km / psg + 10000 * i
    }

}

class Boat(override var name: String = "", override var number: String = "") : Transport {
    override var maxSpeed: Int = 30
    override var passengers: Int = 10
}

fun whatElse(err1: Int) : Int {
    var err = err1 + 1
    if (err == 3){
        println("Повторите снова чуть позже...")
        exitProcess(0)
    }
    else{
        println("Incorrect input, try again.")
    }
    return err
}

fun main() {
    var psgs: Int = 0
    var km: Float = 0F
    var err: Int = 0
    val regexCar = ".ar".toRegex()
    val regexMotorbike = "(B|b)ike$".toRegex()
    val regexAirplane = "plane$".toRegex()
    val regexTrain = ".rain".toRegex()
    val regexBoat = ".oat".toRegex()
    val regaxPsgs = "^[\\d]+$".toRegex()
    println("CALCULATING THE COST OF TRIP")
    while(true) {
        println("How many people are planning to go?")
        val answ1 = readLine().toString()
        if (regaxPsgs.containsMatchIn(answ1) && answ1.toInt() > 0) {
            psgs = answ1.toInt()
            err = 0
            break
        }
        else err = whatElse(err)
    }
    while(true) {
        println("How many km do you need to travel?")
        val answ1 = readLine().toString()
        if (regaxPsgs.containsMatchIn(answ1) && answ1.toFloat() > 0){
            km = answ1.toFloat()
            err = 0
            break

        }
        else err = whatElse(err)
    }

    var i : Int = 0
    var price : Int = 0
    val type : String
    while(true) {
        println("What kind of transport do you need? Car, motorbike, airplane, train or boat.")
        val answ1 = readLine().toString()
        if (regexCar.containsMatchIn(answ1)){
            i = 1
            price = 10
            err = 0
            break
        }
        else if (regexMotorbike.containsMatchIn(answ1)) {
            i = 2
            price = 5
            err = 0
            break
        }
        else if (regexAirplane.containsMatchIn(answ1)){
            i = 3
            price = 50
            err = 0
            break
        }
        else if (regexTrain.containsMatchIn(answ1)){
            i = 4
            err = 0
            break
        }
        else if (regexBoat.containsMatchIn(answ1)){
            i = 5
            price = 20
            err = 0
            break
        }
        else err = whatElse(err)
    }
    if (i == 1 || i == 3 || i == 5){
        while(true) {
            println("What class of service would you like? Economy, business or luxury.")
            val answ1 = readLine().toString()
            if (answ1 != "economy" && answ1 != "business" && answ1 != "luxury"){
                err = whatElse(err)
            }
            else {
                type = answ1
                err = 0
                break
            }
        }
        println("Общая цена поездки: ")
        when(i) {
            1 -> {
                var p : Int = 1
                if(psgs > 4){
                    println("Количество пассажиров не может превышать 4, вам придется заказать больше машин.")
                    if (psgs % 4 != 0) {
                        p = psgs / 4 + 1
                    }
                    else{
                        p = psgs / 4
                    }
                    psgs = 4
                    println("Стоимость перемещения на $p машинах: ")
                }
                print(Car().price(km, price, type, psgs) * p)
                exitProcess(0)
            }
            3 -> {
                print(Airplane().price(km, price, type, psgs))
                exitProcess(0)
            }
            5 -> {
                print(Boat().price(km, price, type, psgs))
                exitProcess(0)
            }
        }
    }
    when(i) {
        2 -> {
            if(psgs > 1){
                println("Количество пассажиров не может превышать 1, вам придется заказать еще мотоцикл.")
                println("Стоимость поездки на $psgs мотоциклах: ")
            }
            print(MotorBike().price(km, price) * psgs)
            exitProcess(0)
        }
        4 -> {
            print(Train().price(km) * psgs)
            exitProcess(0)
        }
    }
}


