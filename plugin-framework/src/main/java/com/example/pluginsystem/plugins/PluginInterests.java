package com.example.pluginsystem.plugins;

public class PluginInterests {

    private boolean isInterestedInLifecycle;
    private boolean isInterestedInActivityResult;
    private boolean isInterestedInPermissionResult;
    private boolean isInterestedBackPressEvent;

    private PluginInterests(){

    }

    public boolean isInterestedInLifecycle() {
        return isInterestedInLifecycle;
    }

    public boolean isInterestedInActivityResult() {
        return isInterestedInActivityResult;
    }

    public boolean isInterestedInPermissionResult() {
        return isInterestedInPermissionResult;
    }

    public boolean isInterestedBackPressEvent() {
        return isInterestedBackPressEvent;
    }

    public static class Builder {

        private boolean isInterestedInLifecycle;
        private boolean isInterestedInActivityResult;
        private boolean isInterestedInPermissionResult;
        private boolean isInterestedBackPressEvent;

        public Builder setInterestedInLifecycle(boolean interestedInLifecycle) {

            this.isInterestedInLifecycle = interestedInLifecycle;
            return this;
        }

        public Builder setInterestedInActivityResult(boolean interestedInActivityResult) {

            this.isInterestedInActivityResult = interestedInActivityResult;
            return this;
        }

        public Builder setInterestedInPermissionResult(boolean interestedInPermissionResult) {

            this.isInterestedInPermissionResult = interestedInPermissionResult;
            return this;
        }

        public Builder setInterestedBackPressEvent(boolean interestedBackPressEvent) {

            this.isInterestedBackPressEvent = interestedBackPressEvent;
            return this;
        }

        public PluginInterests build(){

            PluginInterests pluginInterests = new PluginInterests();
            pluginInterests.isInterestedBackPressEvent = this.isInterestedBackPressEvent;
            pluginInterests.isInterestedInActivityResult = this.isInterestedInActivityResult;
            pluginInterests.isInterestedInLifecycle = this.isInterestedInLifecycle;
            pluginInterests.isInterestedInPermissionResult = this.isInterestedInPermissionResult;
            return pluginInterests;
        }
    }
}
