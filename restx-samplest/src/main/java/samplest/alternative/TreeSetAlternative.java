package samplest.alternative;

import java.util.Set;
import java.util.TreeSet;
import restx.factory.Alternative;
import restx.factory.When;

/**
 * @author apeyrard
 */
@Alternative(to = Set.class)
@When(name = "restx.test.set.impl", value = "TreeSet")
public class TreeSetAlternative extends TreeSet {
}
