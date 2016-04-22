package com.github.fluent.hibernate.internal.util.reflection;

/**
 *
 * @author V.Ladynev
 */
public class NestedSetterAccessorTest {

    private static class Root {

        private Levela levela;

        public Levela getLevela() {
            return levela;
        }

        public void setLevela(Levela levela) {
            this.levela = levela;
        }

    }

    private static class LevelBase {

        private String baseName;

        public String getBaseName() {
            return baseName;
        }

        public void setBaseName(String baseName) {
            this.baseName = baseName;
        }

    }

    private static class Levela extends LevelBase {

        private Levelb levelb;

        public Levelb getLevelb() {
            return levelb;
        }

        public void setLevelb(Levelb levelb) {
            this.levelb = levelb;
        }

    }

    private static class Levelb extends LevelBase {

        private String levelbName;

        public String getLevelbName() {
            return levelbName;
        }

        public void setLevelbName(String levelbName) {
            this.levelbName = levelbName;
        }

    }

}
