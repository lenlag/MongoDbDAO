package dao;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoCursor;
import com.mongodb.client.MongoDatabase;

public class PersonDAO {

	private MongoClient mclient; // Pour pouvoir effectuer des requêtes, il faut créer un objet MongoClient
	private MongoDatabase database;
	MongoCollection<Document> docs;

	
	public PersonDAO() {

		mclient = new MongoClient("localhost"); // on crée le client pour pouvoir effectuer les requetes
		database = mclient.getDatabase("MyDataBase"); // creation de la DB
		docs = database.getCollection("person"); // docs = collection/table

	}

	
	public Map<String, Object> getById(String id) {
		Document doc = null; // document = une ligne
		BasicDBObject search = new BasicDBObject(); // on crée un objet de recherche
		search.put("_id", new ObjectId(id)); // on spécifie que cet objet va chercher sur le id(_id), on lui passe id
												// qui contient id suivant lequel on va chercher
		MongoCursor<Document> cur = docs.find(search).iterator(); // cursor = ResultSet, pour récuperer tous les
																	// Documents de docs(Collection) dont le _id vaut id
																	// // iterator = prepareStatement
		while (cur.hasNext()) {
			doc = cur.next(); // on recupère le Document(s'il y en a un)
			break;
		}
		return doc;
	}

	
	public List<Map<String, Object>> list() {
		List<Map<String, Object>> myList = new ArrayList<>();
		
		Document doc = null;	
		MongoCursor<Document> cur = docs.find().iterator();
		
		while (cur.hasNext()) {
			doc = cur.next();
			myList.add(doc);
			}
		return myList;

	}

	public void deleteById(String id) {
		BasicDBObject search = new BasicDBObject(); // on crée un objet de recherche
		search.put("_id",new ObjectId(id)); // on recherche via id
		docs.deleteOne(search); // on détruit
	}

	public  void create(Map<String, Object> map) {
		Document doc = new Document(map);
		docs.insertOne(doc); // docs = table
		map.put("_id", doc.get("_id")); // on met l’id créé dans la map
	}

	public void update(Map<String, Object> map) {
		String sid = map.get("_id").toString();
		Map<String,Object> old = getById(sid);
		Document newDoc = new Document(map);
		docs.updateOne(new Document(old), new Document("$set", newDoc)); //table.update
		
		
	}

	public void drop (String dbName) {
		mclient.dropDatabase(dbName);
	}
	
}
