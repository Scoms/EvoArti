import java.util.List;
import java.util.Random;

public class Crepe {

	public int _nbOeufs;
	public int _mlLait;
	public int _grammeFarine;
	public int _pinceSel;
	
	
	public Crepe(int nbOeufs, int mlLait, int gFarine, int sel)
	{
		_nbOeufs =  nbOeufs;
		_mlLait = mlLait;
		_grammeFarine = gFarine;
		_pinceSel = sel;
	}
	
	public static Crepe Random()
	{
		Random rand = new Random();
		int oeufs = rand.nextInt(100);
		int sel = rand.nextInt(100);
		int mlLait = rand.nextInt(100000);
		int gFarine = rand.nextInt(100000);
		
		return new Crepe(oeufs,mlLait,gFarine, sel);
	}
	
	public static Crepe faireEnfant(Crepe A, Crepe B)
	{
		Random rand = new Random();
		boolean x = rand.nextBoolean();
		
		int oeufs = x ? A._nbOeufs : B._nbOeufs; 
		x = rand.nextBoolean();
		int sel = x ? A._pinceSel : B._pinceSel;
		x = rand.nextBoolean();
		int lait = x ? A._mlLait : B._mlLait;
		x = rand.nextBoolean();
		int farine = x ? A._mlLait : B._mlLait;
		
		
		return new Crepe(oeufs, lait, farine, sel);
	}
	
	/*
	 * La crepe parfait à une note de 1 
	 * 
	 * 2 oeufs 200 g de farine 200 ml de lait
	 * 4 pincé de sels 
	 * 
	 * 
	 */
	public float eval()
	{
		float res = 0;
		float total = _nbOeufs * 100 + _mlLait + _grammeFarine;
		
		//La liquidité dépend du pourcentage de farine 
		float liquidité = _grammeFarine / total;
		float lquiditéParfait = 1/3;
		
		float equartLiquidité = Math.abs(liquidité - lquiditéParfait);
		
		res -= equartLiquidité;
		
		//Le sel doit etre a 4 / 100
		float ratioSel = total / _pinceSel;
		float selParfait = 4 / 100;
		
		float ecartsel = Math.abs(ratioSel - selParfait);
		
		res -= ecartsel;
		
		return res;
	}
	
	public boolean estMeilleur(Crepe c)
	{
		if(c == null)
			return true;
		
		return c.eval() < this.eval();
	}
	
	public String toString()
	{	
		return String.format("  - oeufs : %d \n  - sel : %d\n  - lait : %dml\n  - farine : %d\n  - eval : %f\n ", _nbOeufs, _pinceSel, _mlLait, _grammeFarine, this.eval());			
	}
	
	public void read()
	{
		System.out.println(
				toString()
		);
	}
	
	public boolean equals(Object crepe)
	{
		return crepe.hashCode() == this.hashCode();
	}
}
