import java.util.Comparator;


class CrepeComparator implements Comparator<Crepe>{

	@Override
	public int compare(Crepe o1, Crepe o2) {
		if(o1.eval() > o2.eval())
			return -1;
		else if(o1.eval() < o2.eval())
			return 1;
		return 0;
	}
	
}