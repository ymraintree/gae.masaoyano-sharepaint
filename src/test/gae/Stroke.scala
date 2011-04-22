package test.gae

import com.google.appengine.api.datastore._
import scala.collection.mutable._

class Stroke {
	var strokeId:String = _
	var canvasId:String = _
	var userId:String = _
	var isAppend:Boolean = _
	var clientTime:Long = _
	var serverTime:Long = _
	var layer:Int = _
	var penProperties:String = _
	var rawText:String = _
}
	
import java.util.logging._

object Stroke {
	private val log:Logger = Logger.getLogger(this.getClass.getName)
	private val dsService = DatastoreServiceFactory.getDatastoreService
	
	def parse(text:String):Stroke = {
		var stroke:Stroke = new Stroke
		stroke.rawText = text
		var props:String = text
		val textArray:Array[String] = text.split(" points:")
		if (1 < textArray.size) props = textArray(0)
		val keyVals:Array[String] = props.split("\\s")
		keyVals.foreach { (s) => 
			val keyVal = s.split(":")
			if (keyVal(0) == "strokeId") stroke.strokeId = keyVal(1)
			else if (keyVal(0) == "canvasId") stroke.canvasId = keyVal(1)
			else if (keyVal(0) == "userId") stroke.userId = keyVal(1)
			else if (keyVal(0) == "isAppend") stroke.isAppend = keyVal(1).asInstanceOf[Boolean]
			else if (keyVal(0) == "clientTime") stroke.clientTime = java.lang.Long.parseLong(keyVal(1))
			else if (keyVal(0) == "layer") stroke.layer = java.lang.Integer.parseInt(keyVal(1))
			else if (keyVal(0) == "penProperties") stroke.penProperties = keyVal(1)
		}
		stroke
	}
	
	def save(text:String):Unit = save(parse(text))

	def save(stroke:Stroke) = {
		stroke.serverTime = System.currentTimeMillis
		dsService.put(scala2java(stroke))
	}
	
	private def scala2java(stroke:Stroke):Entity = {
		val e = new Entity(KeyFactory.createKey("strokeId", stroke.strokeId))
		e.setProperty("canvasId", stroke.canvasId)
		e.setProperty("userId", stroke.userId)
		e.setProperty("isAppend", stroke.isAppend)
		e.setProperty("clientTime", stroke.clientTime)
		e.setProperty("serverTime", stroke.serverTime)
		e.setProperty("layer", stroke.layer)
		e.setProperty("penProperties", stroke.penProperties)
		e.setProperty("rawText", stroke.rawText)
		e
	}
	
	def findAll():Array[String] = {
		val query = new Query("strokeId")
		val queryResult = dsService.prepare(query).asIterator
		var result = new ArrayBuffer[String]
		while (queryResult.hasNext) {
			var e:Entity = queryResult.next
			log.warning("rawText=" + e.getProperty("rawText"))
			result += e.getProperty("rawText").asInstanceOf[String]
		}
		result.toArray
	}
	
}