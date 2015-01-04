import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;


public class Reducer {

	public static List<Crepe> Elitiste(List<Crepe> crepes, int size, int bestProportion, boolean allowDoublons)
	{
		Comparator<Crepe> compara = new CrepeComparator();
		crepes.sort(compara);
		List<Crepe> res = new ArrayList<Crepe>();
		int select = (int) Math.abs(bestProportion * size / 100);
		int select2 = size -select;
		//Add the rest
		ArrayList<Crepe> rest = (ArrayList<Crepe>) ((ArrayList<Crepe>)crepes).clone();
		Collections.shuffle(rest);
		
		if(allowDoublons)
		{
			//Select the best 			
			res = crepes.subList(0, select);
			res.addAll(rest.subList(0, select2));
		}
		else
		{
			res.addAll(FillWithoutDouble(crepes,select));
			res.addAll(FillWithoutDouble(rest,select2));
		}
		
		return res;
	}
	
	private static List<Crepe> FillWithoutDouble(List<Crepe> crepes, int select)
	{
		//System.out.println("sans doublons");
		
		List<Crepe> res = new ArrayList<Crepe>();
		Set<Crepe> crepeSet = new HashSet<Crepe>();
		
		for (Crepe crepe : crepes) {
			crepeSet.add(crepe);
		}

		//crepe set contient les meilleurs sans doublons 
		res.addAll(crepeSet);
		res = res.subList(0, select);
		
		return res;
	}
}


