package data.sequencediagrams;

import java.util.Optional;

public abstract class OptionalCombinedFragment extends CombinedFragment {
	
	public OptionalCombinedFragment(Optional<CombinedFragment> parent) throws IllegalArgumentException {
		super(parent);
	}

	public abstract String getGuard();
}
