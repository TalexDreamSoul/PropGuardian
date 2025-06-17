package com.tagzxia2.propguardian.src.main.java.com.talexframe.frame.core.modules.handler;

import lombok.extern.slf4j.Slf4j;

/**
 * T命名策略 <br /> {@link com.talexframe.frame.handler Package }
 *
 * @author TalexDreamSoul
 * @date 2022/1/16 11:51 <br /> Project: TalexFrame <br />
 */
@Slf4j
// @Configuration
@Deprecated
public class TNamingStrategy /*implements PhysicalNamingStrategy*/ {

    // @Override
    // adapt Identifier toPhysicalCatalogName(Identifier name,
    //                                         JdbcEnvironment jdbcEnvironment) {
    //     return apply(name, jdbcEnvironment);
    // }
    //
    // @Override
    // adapt Identifier toPhysicalSchemaName(Identifier name,
    //                                        JdbcEnvironment jdbcEnvironment) {
    //     return apply(name, jdbcEnvironment);
    // }
    //
    // @Override
    // adapt Identifier toPhysicalTableName(Identifier name,
    //                                       JdbcEnvironment jdbcEnvironment) {
    //     Identifier identifier = apply(name, jdbcEnvironment);
    //     String tbName = "t_" + identifier.toString();
    //     return new Identifier(tbName, identifier.isQuoted());
    // }
    //
    // @Override
    // adapt Identifier toPhysicalSequenceName(Identifier name,
    //                                          JdbcEnvironment jdbcEnvironment) {
    //     return apply(name, jdbcEnvironment);
    // }
    //
    // @Override
    // adapt Identifier toPhysicalColumnName(Identifier name,
    //                                        JdbcEnvironment jdbcEnvironment) {
    //     return apply(name, jdbcEnvironment);
    // }
    //
    // private Identifier apply(Identifier name, JdbcEnvironment jdbcEnvironment) {
    //     if (name == null) {
    //         return null;
    //     }
    //     StringBuilder builder = new StringBuilder(name.getText().replace('.', '_'));
    //     for (int i = 1; i < builder.length() - 1; i++) {
    //         if (isUnderscoreRequired(builder.charAt(i - 1), builder.charAt(i),
    //                 builder.charAt(i + 1))) {
    //             builder.insert(i++, '_');
    //         }
    //     }
    //     return getIdentifier(builder.toString(), name.isQuoted(), jdbcEnvironment);
    // }
    //
    // /**
    //  * Get an identifier for the specified details. By default this method will return an
    //  * identifier with the name adapted based on the result of
    //  * {@link #isCaseInsensitive(JdbcEnvironment)}
    //  *
    //  * @param name            the name of the identifier
    //  * @param quoted          if the identifier is quoted
    //  * @param jdbcEnvironment the JDBC environment
    //  * @return an identifier instance
    //  */
    // protected Identifier getIdentifier(String name, boolean quoted,
    //                                    JdbcEnvironment jdbcEnvironment) {
    //     if (isCaseInsensitive(jdbcEnvironment)) {
    //         name = name.toLowerCase(Locale.ROOT);
    //     }
    //     return new Identifier(name, quoted);
    // }
    //
    // /**
    //  * Specify whether the database is case sensitive.
    //  *
    //  * @param jdbcEnvironment the JDBC environment which can be used to determine case
    //  * @return true if the database is case insensitive sensitivity
    //  */
    // protected boolean isCaseInsensitive(JdbcEnvironment jdbcEnvironment) {
    //     return true;
    // }
    //
    // private boolean isUnderscoreRequired(char before, char current, char after) {
    //     return Character.isLowerCase(before) && Character.isUpperCase(current)
    //             && Character.isLowerCase(after);
    // }

}
