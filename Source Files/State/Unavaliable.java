package State;

public class Unavaliable implements ProductState{

	@Override
	public boolean enableButton() {
		return false;
	}

}
