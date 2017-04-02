package khorrth.discovery.scene;

import java.util.List;
import java.util.ArrayList;

public class Location
{
	protected static final int DEFAULT_CHUNK_SIZE = 16;
	List<ArrayList<Chunk>> chunks = new ArrayList<ArrayList<Chunk>>();

	List<Entity> entities = new ArrayList<Entity>();

	public Location()
	{
		chunks.add(new ArrayList<Chunk>());
	}

	public Chunk getChunk(int columnIndex, int rowIndex)
	{
		return (chunks.get(columnIndex)).get(rowIndex);
	}

	public void addChunk(Chunk chunk)
	{
		chunks.get(0).add(chunk);
	}
}
