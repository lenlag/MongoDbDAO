package main;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dao.PersonDAO;
import junit.framework.TestCase;

public class TestPerson extends TestCase{
	
	PersonDAO pdao;
	
	Map<String, Object> personNatalia; // on crée la map pour y rajouter les objets par la suite(remplir la BDD)
	Map<String, Object> personAngelina;
	Map<String, Object> personBrad;
	
	ArrayList<Map<String, Object>> myMapList; // on crée cette liste pour inclure le map dedans et l'utiliser par la suite dans les TU pour avoir la taille initiale de la liste

	
	@Override
	protected void setUp() throws Exception { //appelé au début de chaque TU=> on crée un jeu de données de test
	pdao = new PersonDAO();
	
	myMapList = new ArrayList<Map<String, Object>>();
	
	personNatalia = new HashMap<>();
	personNatalia.put("firstName", "Natalia");
	personNatalia.put("lastName", "MATHIEU");
	personNatalia.put("age", 19);
	
	myMapList.add(personNatalia);
	pdao.create(personNatalia);
	
	personAngelina = new HashMap<>();
	personAngelina.put("firstName", "Angelina");
	personAngelina.put("lastName", "JOLIE");
	personAngelina.put("age", 45);
	
	myMapList.add(personAngelina);
	pdao.create(personAngelina);
	
	personBrad = new HashMap<>();
	personBrad.put("firstName", "Brad");
	personBrad.put("lastName", "PITT");
	personBrad.put("age", 47);
	
	myMapList.add(personBrad);
	pdao.create(personBrad);
	}

	@Override
	protected void tearDown() throws Exception { //appelé à la fin de chaque TU => vide la BDD
		pdao.drop("MyDataBase");
		
	}
	
	
	public void testList () {
		List<Map<String, Object>> myList = new ArrayList<>();
		myList = pdao.list();
		assertEquals(pdao.list().size(), myMapList.size());
	}
	
	
	public void testCreate () {
		Map<String, Object> personOrlando = new HashMap<>();
		personOrlando.put("firstName", "Orlando");
		personOrlando.put("lastName", "BLOOM");
		personOrlando.put("age", 42);
		
		pdao.create(personOrlando); // je crée le nouvel objet, sans la rajouter dans myMapList
		assertEquals(pdao.list().size(), myMapList.size()+1); // en comparant je fais myMapList+1
	
		String idOrlando = personOrlando.get("_id").toString(); // on extrait l'id de personOrlando
		Map<String, Object> personFromDB = pdao.getById(idOrlando);
		
		assertEquals(personOrlando.get("firstName"), personFromDB.get("firstName"));
		
	}
	
	public void testGetById () {
		String idNatalia = personNatalia.get("_id").toString(); // on extrait id
		Map <String, Object> personFromDB = pdao.getById(idNatalia);// on cherche l'objet via cet id
		assertEquals(personNatalia.get("lastName"), personFromDB.get("lastName")); // on compare le nom de l'objet(extrait via son id) avec le nom de l'objet qu'on a créé auparavant, i.e. on test la recherche via l'id
	}

	
	public void testUpdate () {
		String idNatalia = personNatalia.get("_id").toString(); // on extrait id
		Map <String, Object> personFromDB = pdao.getById(idNatalia);// on cherche l'objet via cet id
		
		assertEquals(personNatalia.get("firstName"), personFromDB.get("firstName"));
		
		personFromDB.put("firstName", "Natasha");
		pdao.update(personFromDB);
		
		Map<String, Object> personFromDB2 = pdao.getById(idNatalia);
		assertEquals(personFromDB.get("firstName"), personFromDB2.get("firstName"));
	
	}
	
	
	public void testDelete() {
		int initialSize = pdao.list().size();
		String idNatalia = personNatalia.get("_id").toString(); // on extrait id
		Map <String, Object> personFromDB = pdao.getById(idNatalia);// on cherche l'objet via cet id
		
		pdao.deleteById(idNatalia); //on supprime l'objet par son id
		personFromDB = pdao.getById(idNatalia); // on essaie de retrouver cet objet par id
		
		int afterDeleteSize = pdao.list().size();
		assertEquals(initialSize-1, afterDeleteSize);
		
		assertNull(personFromDB); // normalement l'objet n'existe plus, car supprimé
	}
	
	
}
