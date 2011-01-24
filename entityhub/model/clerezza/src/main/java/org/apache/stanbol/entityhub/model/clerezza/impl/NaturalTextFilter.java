package org.apache.stanbol.entityhub.model.clerezza.impl;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.clerezza.rdf.core.Literal;
import org.apache.clerezza.rdf.core.PlainLiteral;
import org.apache.clerezza.rdf.core.TypedLiteral;
import org.apache.clerezza.rdf.core.UriRef;
import org.apache.stanbol.entityhub.core.utils.FilteringIterator;
import org.apache.stanbol.entityhub.core.utils.FilteringIterator.Filter;
import org.apache.stanbol.entityhub.servicesapi.defaults.DataTypeEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Filter implementation to be used in combination with {@link FilteringIterator}
 * to return only {@link Literal} values (may be {@link PlainLiteral}s and/or
 * {@link TypedLiteral}s) that confirm to the parsed set of languages.<p>
 * Parsing <code>null</code>, an empty array is interpreted such that any
 * language is accepted. Parsing "" or <code>null</code> as one element of the
 * array indicated that Literals without any language tag are included. This also
 * includes {@link TypedLiteral}s with the data type <code>xsd:string</code>.<p>
 * Note that parsing:<ul>
 * <li> an empty array will result in all Literals (regardless of the language)
 *      are returned
 * <li> an array that contains only the <code>null</code> element will result in
 *      only Literals without any language tag are returned.
 * </ul>
 * 
 * @author Rupert Westenthaler
 *
 */
public class NaturalTextFilter implements Filter<Literal> {
    Logger log = LoggerFactory.getLogger(NaturalTextFilter.class);
    /**
     * The xsd:string data type constant used for TypedLiterals to check if the
     * represent an string value!
     */
    private static UriRef xsdString = new UriRef(DataTypeEnum.String.getUri());
    protected final Set<String> languages;
    private final boolean containsNull;

    public NaturalTextFilter(String...languages){
        if(languages == null || languages.length == 0){
            this.languages = null;
            this.containsNull = true; // if no language is parse accept any (also the default)
        } else {
            Set<String> languageSet = new HashSet<String>(Arrays.asList(languages));
            if(languageSet.remove("")){
                /*
                 * Parsing "" as language needs to be interpreted as parsing
                 * null
                 */
                languageSet.add(null);
            }
            this.languages = Collections.unmodifiableSet(languageSet);
            this.containsNull = this.languages.contains(null);
        }
    }
    @Override
    public boolean isValid(Literal value) {
        if (value instanceof PlainLiteral){
           if(languages == null) { //no language restrictions
                return true; //return any Plain Literal
            } else {
                String literalLang = ((PlainLiteral) value).getLanguage() == null ?
                    null : ((PlainLiteral) value).getLanguage().toString();
                return languages.contains(literalLang);
            }
        } else if(value instanceof TypedLiteral){
            /*
             * if the null language is active, than we can also return
             * "normal" literals (with no known language). This includes
             * Types literals with the data type xsd:string
             */
            return containsNull && ((TypedLiteral)value).getDataType().equals(xsdString);
        } else {// unknown Literal type -> filter + warning
            log.warn(String.format("Unknown LiteralType %s (lexicalForm=\"%s\") -> ignored! Pleas adapt this implementation to support this type!",
                value.getClass(),value.getLexicalForm()));
            return false;
        }
    }
}
