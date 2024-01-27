package me.mattco.serenityos.gml

import me.mattco.serenityos.common.DSLColorSettingsPage
import me.mattco.serenityos.ipc.IPCSyntaxHighlighter

class GMLColorSettingsPage  : DSLColorSettingsPage(GMLLanguage) {
    override fun getIcon() = GMLLanguage.FILE_ICON

    override fun getHighlighter() = IPCSyntaxHighlighter()

    override fun getTemplate() = """
        // A SystemMonitor GML (abbreviated)
        // This makes use of quite some custom objects and properties.
        @NS_NAME{GUI}::CLASS_NAME{Widget} {
            PROPERTY{fill_with_background_color}: BOOLEAN{true}
            PROPERTY{layout}: @NS_NAME{GUI}::CLASS_NAME{VerticalBoxLayout} {}

            @NS_NAME{GUI}::CLASS_NAME{Widget} {
                PROPERTY{layout}: @NS_NAME{GUI}::CLASS_NAME{VerticalBoxLayout} {
                    PROPERTY{margins}: [0, 4, 4]
                }

                @NS_NAME{GUI}::CLASS_NAME{TabWidget} {
                    PROPERTY{name}: "main_tabs"

                    @NS_NAME{GUI}::CLASS_NAME{Widget} {
                        PROPERTY{title}: "Processes"
                        PROPERTY{name}: "processes"

                        @NS_NAME{GUI}::CLASS_NAME{TableView} {
                            PROPERTY{name}: "process_table"
                            PROPERTY{column_headers_visible}: BOOLEAN{true}
                        }
                    }

                    @NS_NAME{GUI}::CLASS_NAME{Widget} {
                        PROPERTY{title}: "Performance"
                        PROPERTY{name}: "performance"
                        PROPERTY{background_role}: "Button"
                        PROPERTY{fill_with_background_color}: BOOLEAN{true}
                        PROPERTY{layout}: @NS_NAME{GUI}::CLASS_NAME{VerticalBoxLayout} {
                            PROPERTY{margins}: [4]
                        }

                        @NS_NAME{GUI}::CLASS_NAME{GroupBox} {
                            PROPERTY{title}: "CPU usage"
                            PROPERTY{name}: "cpu_graph"
                            PROPERTY{layout}: @NS_NAME{GUI}::CLASS_NAME{VerticalBoxLayout} {}
                        }

                        @NS_NAME{GUI}::CLASS_NAME{GroupBox} {
                            PROPERTY{title}: "Memory usage"
                            PROPERTY{fixed_height}: 120
                            PROPERTY{layout}: @NS_NAME{GUI}::CLASS_NAME{VerticalBoxLayout} {
                                PROPERTY{margins}: [6]
                            }

                            @SystemMonitor::GraphWidget {
                                PROPERTY{stack_values}: BOOLEAN{true}
                                PROPERTY{name}: "memory_graph"
                            }
                        }

                        @SystemMonitor::MemoryStatsWidget {
                            PROPERTY{name}: "memory_stats"
                            // A custom property that refers back up to the GraphWidget for the memory graph.
                            PROPERTY{memory_graph}: "memory_graph"
                        }
                    }

                    @SystemMonitor::StorageTabWidget {
                        PROPERTY{title}: "Storage"
                        PROPERTY{name}: "storage"
                        PROPERTY{layout}: @NS_NAME{GUI}::CLASS_NAME{VerticalBoxLayout} {
                            PROPERTY{margins}: [4]
                        }

                        @NS_NAME{GUI}::CLASS_NAME{TableView} {
                            PROPERTY{name}: "storage_table"
                        }
                    }

                    @SystemMonitor::NetworkStatisticsWidget {
                        PROPERTY{title}: "Network"
                        PROPERTY{name}: "network"
                    }
                }
            }

            @NS_NAME{GUI}::CLASS_NAME{Statusbar} {
                PROPERTY{segment_count}: 3
                PROPERTY{name}: "statusbar"
            }
        }
    """.trimIndent()

    override fun getAdditionalHighlightingTagToDescriptorMap() = mapOf(
        "NS_NAME" to Highlights.NAMESPACE_NAME,
        "CLASS_NAME" to Highlights.COMPONENT_NAME,
        "PROPERTY" to Highlights.PROPERTY_NAME,
        "BOOLEAN" to Highlights.BOOLEAN
    )

    override fun getAttributes() = mapOf(
        "Comments" to Highlights.COMMENT,

        "Identifiers//Namespaces" to Highlights.NAMESPACE_NAME,
        "Identifiers//Classes" to Highlights.COMPONENT_NAME,
        "Identifiers//Properties" to Highlights.PROPERTY_NAME,

        "Literals//Numbers" to Highlights.NUMBER,
        "Literals//Strings" to Highlights.STRING,
        "Literals//Booleans" to Highlights.BOOLEAN,

        "Operators and Delimiters//Braces" to Highlights.BRACES,
        "Operators and Delimiters//Brackets" to Highlights.BRACKETS,
        "Operators and Delimiters//Comma" to Highlights.COMMA,
        "Operators and Delimiters//Namespace Separator" to Highlights.NAMESPACE,
        "Operators and Delimiters//At" to Highlights.AT,
        "Operators and Delimiters//Colon" to Highlights.COLON,
    )
}
