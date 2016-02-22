package com.example.animaldb;

public class AnimalCode
{
	public int ANIMAL_CD_ID;
	public String ANIMAL_CD;
	public String ANIMAL_DESC;
	
	@Override
	public String toString() 
	{
		return ANIMAL_DESC;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((ANIMAL_CD == null) ? 0 : ANIMAL_CD.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AnimalCode other = (AnimalCode) obj;
		if (ANIMAL_CD == null)
		{
			if (other.ANIMAL_CD != null)
				return false;
		} else if (!ANIMAL_CD.equals(other.ANIMAL_CD))
			return false;
		return true;
	}
	
}
