import java.util.ArrayList;
import java.util.List;
import java.util.Random;


public class Coeur extends Thread {

	public static List<Crepe> _cacheGlobal;
	public static Crepe _cacheLocal;
	public static Crepe p1;
	public static Crepe p2;
	public static Crepe value;
	public static boolean CycleIs;
	
	public static int id;
	
	public Coeur(int id)
	{
		this.id = id;
	}
	
	public Crepe GetRes()
	{
		return value;
	}
	
	@Override
    public void run() {

		value = Crepe.Random();
		Crepe enfant = Crepe.faireEnfant(p1, p2);
		ArrayList<Crepe> toCompare = new ArrayList<Crepe>();
		toCompare.add(p1);		
		toCompare.add(p2);		
		toCompare.add(enfant);
		
		Crepe meilleurIndividu = getMeilleur(toCompare);

		
		if(meilleurIndividu.estMeilleur(_cacheLocal))
		{
			_cacheLocal = meilleurIndividu;
		}
		
		Random rand = new Random();
		
		int x = rand.nextInt() % 3;
		
		switch (x) {
		case 0:
			value = p1;
			break;

		case 1:
			value = p2;
			break;

		case 2:
			value = enfant;
			break;
		}
		
		
		if(CycleIs)
		{
			toCompare.add(_cacheLocal);
			value = getMeilleur(toCompare);
		}
    }
	
	public Crepe renvoieMeilleur(Crepe parentA, Crepe parentB, boolean isCycle)
	{

		Crepe res = Crepe.Random();

		Crepe enfant = Crepe.faireEnfant(parentA, parentB);
		
		ArrayList<Crepe> toCompare = new ArrayList<Crepe>();
		
		toCompare.add(parentA);		
		toCompare.add(parentB);		
		toCompare.add(enfant);
		
		Crepe meilleurIndividu = getMeilleur(toCompare);

		
		if(meilleurIndividu.estMeilleur(_cacheLocal))
		{
			_cacheLocal = meilleurIndividu;
		}
		
		Random rand = new Random();
		
		int x = rand.nextInt() % 3;
		
		switch (x) {
		case 0:
			res = parentA;
			break;

		case 1:
			res = parentB;
			break;

		case 2:
			res = enfant;
			break;
		}
		
		
		if(isCycle)
		{
			toCompare.add(_cacheLocal);
			res = getMeilleur(toCompare);
		}
		
		return res;
	}
	
	public static Crepe getMeilleur(List<Crepe> li)
	{
		Crepe res = li.get(0);
		float bestEval = 0;
		for (Crepe crepe : li) {
			float eval = crepe.eval();
			if(eval > bestEval)
			{
				bestEval = eval;
				res = crepe;
			}
		}
		
		return res;
	}
}
