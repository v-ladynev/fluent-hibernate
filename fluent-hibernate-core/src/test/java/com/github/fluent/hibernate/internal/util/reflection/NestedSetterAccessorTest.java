package com.github.fluent.hibernate.internal.util.reflection;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.Before;
import org.junit.Test;

/**
 *
 * @author V.Ladynev
 */
public class NestedSetterAccessorTest {

    private RootLevel root;

    @Before
    public void setUp() {
        root = new RootLevel();
    }

    @Test
    public void testDirectLevel() {
        NestedSetterAccessor.getSetter(RootLevel.class, "rootName").set(root, "rootName");
        NestedSetterAccessor.getSetter(RootLevel.class, "levela.levelaName").set(root,
                "levelaName");
        NestedSetterAccessor.getSetter(RootLevel.class, "levela.levelb.levelbName").set(root,
                "levelbName");

        assertThat(root.getRootName()).isEqualTo("rootName");

        assertThat(root.getLevela()).isNotNull();
        assertThat(root.getLevela().getLevelaName()).isEqualTo("levelaName");
        assertThat(root.getLevela().getLevelb()).isNotNull();
        assertThat(root.getLevela().getLevelb().getLevelbName()).isEqualTo("levelbName");
    }

    @Test
    public void testInheretedLevel() {
        NestedSetterAccessor.getSetter(RootLevel.class, "baseName").set(root, "baseName");
        NestedSetterAccessor.getSetter(RootLevel.class, "levela.baseName").set(root,
                "levelaBaseName");
        NestedSetterAccessor.getSetter(RootLevel.class, "levela.levelb.baseName").set(root,
                "levelbBaseName");

        assertThat(root.getBaseName()).isEqualTo("baseName");

        assertThat(root.getLevela()).isNotNull();
        assertThat(root.getLevela().getBaseName()).isEqualTo("levelaBaseName");
        assertThat(root.getLevela().getLevelb()).isNotNull();
        assertThat(root.getLevela().getLevelb().getBaseName()).isEqualTo("levelbBaseName");
    }

    public static class RootLevel extends LevelBase {

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
