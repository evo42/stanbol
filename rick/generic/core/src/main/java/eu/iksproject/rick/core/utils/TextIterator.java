package eu.iksproject.rick.core.utils;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import eu.iksproject.rick.servicesapi.model.Text;
import eu.iksproject.rick.servicesapi.model.ValueFactory;

public class TextIterator extends AdaptingIterator<Object,Text> implements Iterator<Text> {
//    protected final Iterator<?> it;
//    private Text next;
//    private Boolean hasNext;
//    protected final Set<String> languages;
//    private final boolean isNullLanguage;
//    protected final ValueFactory valueFactory;
    /**
     * Creates an instance that iterates over values and returns {@link Text}
     * instances that confirm to the active languages. If no languages are parsed
     * or <code>null</code> is parsed as a language, this Iterator also creates
     * and returns {@link Text} instances for {@link String} values.
     * @param valueFactory the factory used to create text instances for String values
     * @param it the iterator
     * @param languages The active languages or no values to accept all languages
     */
    public TextIterator(ValueFactory valueFactory,Iterator<Object> it,String...languages){
        super(it,new TextAdapter(valueFactory, languages),Text.class);
//        if(it == null){
//            throw new IllegalArgumentException("Parsed iterator MUST NOT be NULL!");
//        }
//        this.it = it;
//        if(valueFactory == null){
//            throw new IllegalArgumentException("Parsed ValueFactory MUST NOT be NULL!");
//        }
//        this.valueFactory = valueFactory;
//        if(languages != null && languages.length>0){
//            this.languages = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(languages)));
//            isNullLanguage = this.languages.contains(null);
//        } else {
//            this.languages = null;
//            isNullLanguage = true;
//        }
    }
    private static class TextAdapter implements Adapter<Object,Text>{
        protected final Set<String> languages;
        private final boolean isNullLanguage;
        protected final ValueFactory valueFactory;
        
        public TextAdapter(ValueFactory valueFactory,String...languages) {
            if(valueFactory == null){
                throw new IllegalArgumentException("Parsed ValueFactory MUST NOT be NULL!");
            }
            this.valueFactory = valueFactory;
            if(languages != null && languages.length>0){
                this.languages = Collections.unmodifiableSet(new HashSet<String>(Arrays.asList(languages)));
                isNullLanguage = this.languages.contains(null);
            } else {
                this.languages = null;
                isNullLanguage = true;
            }
            
        }

        @Override
        public Text adapt(Object value, Class<Text> type) {
            if(value instanceof Text){
                Text text = (Text)value;
                if(languages == null || languages.contains(text.getLanguage())){
                    return text;
                } else { //language does not fit -> filter
                    return null;
                }
            } else if(isNullLanguage && value instanceof String){
                return valueFactory.createText((String)value);
            }  else {//type does not fit -> filter
                return null;
            }
        }
        
    }
//    @Override
//    public final void remove() {
//        /*
//         * TODO: Any Iterator that filters elements of the underlying Iterator
//         * needs to call Iterator#next() in the underlying Iterator to get the
//         * next element that confirms with the filter.
//         * However the Iterator#remove() is defined as removing the last element
//         * to be returned by Iterator#next(). Therefore calling hasNext would
//         * change the element to be removed by this method.
//         * Currently I do not know a way around that but I would also like to
//         * keep the remove functionality for Iterator that filter elements of an
//         * underlying Iterator. To prevent unpredictable behaviour in such cases 
//         * I throw an IllegalStateException in such cases.
//         * This decision assumes, that in most usage scenarios hasNext will not
//         * likely be called before calling remove and even in such cases
//         * it will be most likely be possible to refactor the code to confirm
//         * with this restriction.
//         * I hope this will help developers that encounter this exception to
//         * modify there code!
//         * If someone has a better Idea how to solve this please let me know!
//         * best 
//         * Rupert Westenthaler
//         */
//        if(hasNext!= null){
//            throw new IllegalStateException("Remove can not be called after calling hasNext() because this Method needs to call next() on the underlying Interator and therefore would change the element to be removed :(");
//        }
//        it.remove();
//    }
//
//    @Override
//    public final Text next() {
//        hasNext(); //call hasNext (to init next Element if not already done)
//        if(!hasNext){
//            throw new NoSuchElementException();
//        } else {
//            Text current = next;
//            hasNext = null;
//            return current;
//        }
//    }
//
//    @Override
//    public final boolean hasNext() {
//        if(hasNext == null){ // only once even with multiple calls
//            next = prepareNext();
//            hasNext = next != null;
//        }
//        return hasNext;
//    }
//    protected Text prepareNext(){
//        Object check;
//        while(it.hasNext()){
//            check = it.next();
//            if(check instanceof Text){
//                Text text = (Text)check;
//                if(languages == null || languages.contains(text.getLanguage())){
//                    return text;
//                }
//            } else if(isNullLanguage && check instanceof String){
//                return valueFactory.createText((String)check);
//            } //type does not fit -> ignore
//        }
//        //no more element and still nothing found ... return end of iteration
//        return null;
//    }
}
