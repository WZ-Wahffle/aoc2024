import java.net.CookieHandler
import java.net.CookieManager
import java.net.HttpCookie
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse.BodyHandlers
import java.nio.file.Files
import java.nio.file.Path
import java.util.Calendar
import kotlin.io.path.writeText
import kotlin.system.exitProcess
import kotlin.system.measureNanoTime
import kotlin.time.Duration
import kotlin.time.DurationUnit
import kotlin.time.ExperimentalTime

@OptIn(ExperimentalTime::class)
fun main() {
    val now = Calendar.getInstance()
    if (now.get(Calendar.YEAR) == 2024 &&
        now.get(Calendar.MONTH) == Calendar.DECEMBER &&
        now.get(Calendar.DATE) <= 25
    ) {
        val day = now.get(Calendar.DATE)
        if (!Files.exists(Path.of("resources/Day${day}Input.txt"))) {
            CookieHandler.setDefault(CookieManager())
            val cookie = HttpCookie(
                "session",
                Files.readAllBytes(Path.of("resources/.cookie")).decodeToString()
            )
            cookie.path = "/"
            cookie.version = 0
            (CookieHandler.getDefault() as CookieManager).cookieStore.add(
                URI.create("https://adventofcode.com"),
                cookie
            )
            val client = HttpClient.newBuilder().cookieHandler(CookieHandler.getDefault()).build()
            val req =
                HttpRequest.newBuilder(URI.create("https://adventofcode.com/2024/day/${day}/input"))
                    .GET().build()
            val res = client.send(req, BodyHandlers.ofString())
            if (res.statusCode() != 500) {
                Files.createFile(Path.of("resources/Day${day}Input.txt")).writeText(res.body())
            } else {
                println("Token expired!")
                exitProcess(1)
            }
        }

        val input = Files.readAllLines(Path.of("resources/Day${day}Input.txt")) ?: listOf()
        val clazz = ClassLoader.getSystemClassLoader().loadClass("Day${day}")
        val instance = clazz.getConstructor().newInstance()

        val firstExample = try {
            Files.readAllLines(Path.of("resources/Day${day}Ex1.txt"))
        } catch (_: Exception) {
            try {
                Files.readAllLines(Path.of("resources/Day${day}Ex.txt"))
            } catch (_: Exception) {
                null
            }
        }
        val secondExample = try {
            Files.readAllLines(Path.of("resources/Day${day}Ex2.txt")) ?: null
        } catch (_: Exception) {
            try {
                Files.readAllLines(Path.of("resources/Day${day}Ex.txt"))
            } catch (_: Exception) {
                null
            }
        }

        if (firstExample != null) {
            println("Part 1 Example: ")
            println("Took " + Duration.convert(measureNanoTime {
                clazz.getDeclaredMethod("part1", List::class.java).invoke(instance, firstExample)
            }.toDouble(), DurationUnit.NANOSECONDS, DurationUnit.MILLISECONDS) + " ms\n")

        }
        println("Part 1: ")
        println("Took " + Duration.convert(measureNanoTime {
            clazz.getDeclaredMethod("part1", List::class.java).invoke(instance, input)
        }.toDouble(), DurationUnit.NANOSECONDS, DurationUnit.MILLISECONDS) + " ms\n")

        if (secondExample != null) {
            println("Part 2 Example: ")
            println("Took " + Duration.convert(measureNanoTime {
                clazz.getDeclaredMethod("part2", List::class.java).invoke(instance, secondExample)
            }.toDouble(), DurationUnit.NANOSECONDS, DurationUnit.MILLISECONDS) + " ms\n")
        }

        println("Part 2: ")
        println("Took " + Duration.convert(measureNanoTime {
            clazz.getDeclaredMethod("part2", List::class.java).invoke(instance, input)
        }.toDouble(), DurationUnit.NANOSECONDS, DurationUnit.MILLISECONDS) + " ms\n")
    }
}
