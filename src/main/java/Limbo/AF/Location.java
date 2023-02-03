package Limbo.AF;

public class Location {
	String name;
	int x, y, z;
	
	public Location(int x, int y, int z) {
		set(x, y, z);
	}
	
	public Location(double x, double y, double z) {
		setConvert(x, y, z);
	}
	
	public Location(org.bukkit.Location loc) {
		setConvert(loc.getX(), loc.getY(), loc.getZ());
	}
	

	public void set(int x, int y, int z) {
		this.x = x;
		this.y = y;
		this.z = z;
		this.name = this.x+":"+this.y+":"+this.z;
	}
	
	public void setConvert(double x, double y, double z) {
		this.x = (int) x;
		this.y = (int) y;
		this.z = (int) z;
		this.name = this.x+":"+this.y+":"+this.z;
	}
	
	public int getX() {
		return this.x;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getZ() {
		return this.z;
	}
	
	public String getName() {
		return this.name;
	}
	
	public Double distance(org.bukkit.Location loc) {
		double x = this.x - loc.getX();
		double y = this.y - loc.getY();
		double z = this.z - loc.getZ();
		return Math.sqrt(x*x + y*y + z*z);
	}
}
