class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello1111, ${platform.name}!"
    }
}