package khorrth.discovery.scene;

import java.util.List;
import java.util.ArrayList;
// import netshock.discovery.*;

public class Chunk
{
	protected static final int DEFAULT_TILE_SIZE = 32;

	public List<Structure> structures;

	public Chunk()
	{
		structures = new ArrayList<>();
	}

	public void addStructure(Structure structure)
	{
		structures.add(structure);
	}
}
