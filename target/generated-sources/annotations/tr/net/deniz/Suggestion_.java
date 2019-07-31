package tr.net.deniz;

import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value = "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor")
@StaticMetamodel(Suggestion.class)
public abstract class Suggestion_ {

	public static volatile SingularAttribute<Suggestion, Long> fileIdRef;
	public static volatile SingularAttribute<Suggestion, Boolean> hasFile;
	public static volatile SingularAttribute<Suggestion, Long> suggestionId;
	public static volatile SingularAttribute<Suggestion, String> byUserName;
	public static volatile SingularAttribute<Suggestion, Boolean> isLocked;
	public static volatile SingularAttribute<Suggestion, String> title;
	public static volatile SingularAttribute<Suggestion, String> lockedById;
	public static volatile SingularAttribute<Suggestion, String> suggestionText;

}

