package samplest.alternative;

import java.util.LinkedList;
import java.util.List;
import javax.inject.Named;
import restx.factory.Alternative;
import restx.factory.When;

/**
 * @author apeyrard
 */
@Alternative(to = List.class)
@When(name = "restx.test.list.impl", value = "LinkedList")
public class LinkedListAlternative extends LinkedList {
}
