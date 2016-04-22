package com.github.fluent.hibernate.internal.util.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NestedSetterAccessorTest {

    private Root root;

    @Before
    public void setUp() {
        root = new Root();
    }

    @Test
    public void testDirectLevel() {
        NestedSetterAccessor.getSetter(Root.class, "rootName").set(root, "rootName");
        NestedSetterAccessor.getSetter(Root.class, "levela.levelaName").set(root, "levelaName");

        assertThat(root.getRootName()).isEqualTo("rootName");

        assertThat(root.getLevela()).isNotNull();
        assertThat(root.getLevela().getLevelaName()).isEqualTo("levelaName");
    }

    public static class Root {

        private Levela levela;

        private String rootName;

        public Levela getLevela() {
            return levela;
        }

        public void setLevela(Levela levela) {
            this.levela = levela;
        }

        public String getRootName() {
            return rootName;
        }

        public void setRootName(String rootName) {
            this.rootName = rootName;
        }

    }

    public static class LevelBase {

        private String baseName;

        public String getBaseName() {
            return baseName;
        }

        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }

    }

    public static class Levela extends LevelBase {

        private Levelb levelb;

        private String levelaName;

        public Levelb getLevelb() {
            return levelb;
        }

        public void setLevelb(Levelb levelb) {
            this.levelb = levelb;
        }

        public String getLevelaName() {
            return levelaName;
        }

        public void setLevelaName(String levelaName) {
            this.levelaName = levelaName;
        }

    }

    public static class Levelb extends LevelBase {

        private String levelbName;

        public String getLevelbName() {
            return levelbName;
        }

        public void setLevelbName(String levelbName) {
            this.levelbName = levelbName;
        }

    }

}
