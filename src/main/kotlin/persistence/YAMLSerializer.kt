package persistence

import org.yaml.snakeyaml.DumperOptions
import org.yaml.snakeyaml.Yaml
import kotlin.Throws
import models.Collection
import java.io.File
import java.io.FileReader
import java.io.FileWriter

class YamlSerializer(private val file: File) : Serializer {
    @Throws(Exception::class)
    override fun read(): Any {
        val yaml = Yaml()
        val inputStream = FileReader(file)
        val obj = yaml.load(inputStream) as Any
        inputStream.close()
        return obj
    }

    @Throws(Exception::class)
    override fun write(obj: Any?) {
        val yaml = Yaml(DumperOptions().apply { defaultFlowStyle = DumperOptions.FlowStyle.BLOCK })
        val outputStream = FileWriter(file)
        yaml.dump(obj, outputStream)
        outputStream.close()
    }
}