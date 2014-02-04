package gpigb.store;

import gpigb.data.RecordSet;

public class MySQLStore implements Store
{

	@Override
	public boolean read(RecordSet<?> unpopulated)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean write(RecordSet<?> data)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean delete(RecordSet<?> items)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	private void connect()
	{
		
	}
}
