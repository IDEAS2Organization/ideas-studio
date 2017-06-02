// App. Initialization:
jQuery(function () {

    toggleAdvancedMode = function () {

        var operationId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(EditorManager.currentUri));
        var allFiles = $(".dynatree-title");
        // 
        if (DEFAULT_FILTER_FILES === false) {
            // Disable filter
            allFiles.each(function () {
                if (CONFIG_FILE_EXTENSIONS_TO_FILTER.indexOf($(this).text().split('.').pop()) !== -1) {
                    $(this).closest("li").show();
                }
            });

            // Show inspector
            $("#editorToggleInspector").show();

            if (EditorManager.currentUri !== "") {
                // Show format tabs
                if ($("#editorFooter li").length > 0)
                    $("#editorFooter").show();
                if ($("#editorWrapper").css("bottom") == "0px") {
                    $("#editorWrapper").css("bottom", "28px");
                    // Show console
                    DescriptionInspector.expandConsole('contract');
                }
            }

            // Show advanced operations
            if (!!opMap && typeof opMap === "object") {
                Object.keys(opMap).forEach(function (e) {
                    if (opMap[e].advanced === true) {
                        $("#" + opMap[e].id).show();
                    }
                })
            }

            DEFAULT_FILTER_FILES = !DEFAULT_FILTER_FILES;

        } else {
            // Enable filter
            allFiles.each(function () {
                var isFolder = $(this).closest("span").hasClass("dynatree-folder");
                if (!isFolder && CONFIG_FILE_EXTENSIONS_TO_FILTER.indexOf($(this).text().split('.').pop()) !== -1) {
                    $(this).closest("li").hide();
                    //TODO: close files
                }
            });

            // Hide inspector
            if ($("#editorToggleInspector").hide().hasClass("hdd"))
                toggleInspector();

            if (EditorManager.currentUri !== "") {
                // Hide format tabs
                $("#editorFooter").hide();
                $("#editorWrapper").css("bottom", "0");

                // Hide console
                DescriptionInspector.expandConsole('hide');
            }

            // Hide advanced operations
            if (!!opMap && typeof opMap === "object") {
                Object.keys(opMap).forEach(function (e) {
                    if (opMap[e].advanced === true) {
                        $("#" + opMap[e].id).hide();
                    }
                })
            }

            DEFAULT_FILTER_FILES = !DEFAULT_FILTER_FILES;
        }
    };

    $('.dropdown-toggle').click(function (e) {
        e.preventDefault();
        setTimeout($.proxy(function () {
            if ('ontouchstart' in document.documentElement) {
                $(this).siblings('.dropdown-backdrop').off().remove();
            }
        }, this), 0);
    });

    // Menu toggler:
    $("#menuToggler").click(function () {
        toggleMenu();
    });
    $("#appMainContentBlocker").click(function () {
        toggleMenu();
    });

    $(".addWorkspace").click(function () {
        showContentAsModal("app/modalWindows/createWorkspace", function () {
            var workspaceName = normalizeWSName($("#modalCreationField input").val());
            var description = $("#descriptionInput textarea").val();
            var tags = $("#tagsInput textarea").val();

            if (workspaceName !== "") {
                $("#workspacesNavContainer li").removeClass("active");
                WorkspaceManager.createWorkspace(workspaceName, description, tags, function () {
                    AppPresenter.loadSection("editor", workspaceName, function () {
                        WorkspaceManager.loadWorkspace();
                    });
                });
            } else {
                alert("Unable to create workspace");
            }

        });
        // Update workspace name from file
        setTimeout(function () {
            $(".modal-body").find('[data-toggle="collapse"][data-target="#zipFileDiv"]').unbind("click").on("click", function () {
                $("#zipFile").unbind("change").on("change", function () {
                    var fpath = $(this).val().replace(/\\/g, '/');
                    var fname = fpath.substring(fpath.lastIndexOf('/') + 1, fpath.lastIndexOf('.'));
                    $("#modalCreationField input").val(fname);
                });
            });
        }, 500);
    });

    WorkspaceManager.readSelectedWorkspace(function (sws) {
        var sws = WorkspaceManager.getSelectedWorkspace();
        WorkspaceManager.getWorkspaces(function (wss) {
            if (!sws) {
                if (wss.length > 0) {
                    WorkspaceManager.setSelectedWorkspace(wss[0].name);
                    WorkspaceManager.loadWorkspace();
                } else if (wss.length == 0) {
                    CommandApi.importDemoWorkspace("SampleWorkspace",
                            "SampleWorkspace");
                }
            } else {
                WorkspaceManager.loadWorkspace();
            }
        });

    });

    // Show the version info content
    $("#version").click(function () {
        $("#versions").show();
    });
    $("#vClose").click(function () {
        $("#versions").hide();
    });



});

// Modal:
var showModal = function (title, content, primaryText, primaryHandler,
        cancelHandler, closeHandler) {
    if ($("#appGenericModal"))
        $("#appGenericModal").remove();
    $("body")
            .append(
                    '<!-- Modal for all app -->'
                    + '<div class="modal" id="appGenericModal" data-backdrop="true" data-keyboard="true">'
                    + '<div class="modal-dialog">'
                    + '<div class="modal-content">'
                    + '<div class="modal-header">'
                    + '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>'
                    + '<h4 class="modal-title">Modal title</h4></div>'
                    + '<div class="modal-body">  ...</div>'
                    + '<div class="modal-footer">'
                    + '  <a data-dismiss="modal" class="btn dismiss">Close</a>'
                    + '  <a class="btn btn-primary continue"></a></div>  </div></div></div>');
    $('#appGenericModal .modal-title').text(title);
    $('#appGenericModal .modal-body').empty();
    $('#appGenericModal .modal-body').append(content);
    $('#appGenericModal .continue').text(primaryText);
    $('#appGenericModal .close').click(cancelHandler);
    $('#appGenericModal .dismiss').click(cancelHandler);
    $('#appGenericModal .continue').click(primaryHandler);
    $('#appGenericModal').modal({
        show: true
    });
    $("#appGenericModal").click(function () {
        cancelHandler();
    });
    $(".modal-dialog").click(function (event) {
        event.stopPropagation();
    });
};

var showContentAsModal = function (url, primaryHandler, cancelHandler,
        closeHandler) {
    if ($("#appGenericModal"))
        $("#appGenericModal").remove();
    $.ajax({
        "url": url,
        success: function (result, textStatus, request) {
            $("body").append(result);
            $('#appGenericModal .close').click(cancelHandler);
            $('#appGenericModal .dismiss').click(cancelHandler);
            $('#appGenericModal .continue').click(primaryHandler);
            $('#appGenericModal').modal({
                show: true
            });
            $('.focusedInput').focus();
            $('.modal-content').keypress(function (e) {
                var code = e.which;
                if (code == 13) {
                    primaryHandler();
                }
            });
        }
    });

}

var showError = function (title, content, primaryHandler) {
    if ($("#appGenericError"))
        $("#appGenericError").remove();
    $("body")
            .append(
                    '<!-- Modal for all app --><div class="modal" id="appGenericError"><div class="modal-dialog">  <div class="modal-content"><div class="modal-header">  <button type="button" class="close" data-dismiss="modal" aria-hidden="true">×</button>  <h4 class="modal-title">Modal title</h4></div><div class="modal-body"> </div><div class="modal-footer">   <a class="btn btn-primary continue"></a></div>  </div></div></div>');
    $('#appGenericError .modal-title').text(title);
    $('#appGenericError .modal-body').html(content);
    $('#appGenericError .continue').text("OK");

    $('#appGenericError .continue').click(primaryHandler);
    $('#appGenericError').modal({
        show: true
    });
};

var hideError = function () {
    $('#appGenericError').modal('hide');
    $(".modal-backdrop").remove();
};

var hideModal = function () {
    $('#appGenericModal').modal('hide');
};

var toggleMenu = function () {
    var leftMenu = $("#appLeftMenu");
    var contentBlocker = $("#appMainContentBlocker");
    var appMainContentWrapper = $("#appMainContent");
    if (leftMenu.hasClass("menuClose")) {
        leftMenu.addClass("menuOpen");
        leftMenu.removeClass("menuClose");
        contentBlocker.addClass("visible");
        contentBlocker.removeClass("hidden");
        appMainContentWrapper.css({"-webkit-animation": "appMainContent_blur 1s normal forwards", "cursor": "pointer"});
    } else {
        leftMenu.addClass("menuClose");
        leftMenu.removeClass("menuOpen");
        contentBlocker.addClass("hidden");
        contentBlocker.removeClass("visible");
        appMainContentWrapper.css({"-webkit-animation": "appMainContent_focus 1s normal forwards", "cursor": ""});
    }
};

var compareObjects = function equals(obj1, obj2) {
    function _equals(obj1, obj2) {
        var clone = $.extend(true, {}, obj1),
                cloneStr = JSON.stringify(clone, function (key, value) {
                    if (key === "$$hashKey") {
                        return undefined;
                    }

                    return value;
                });
        return cloneStr === JSON.stringify($.extend(true, clone, obj2), function (key, value) {
            if (key === "$$hashKey") {
                return undefined;
            }

            return value;
        });
    }

    return _equals(obj1, obj2) && _equals(obj2, obj1);
}

var normalizeWSName = function (s) {
    return s.replace(/\s+/g, "-").replace(/[|&;:$%@"<>()+,]/g, "");
}


var EditorCheckerIcon = {
    loading: function () {
        this.fitScrollbar();
        $(".ball-clip-rotate").show();
        $(".editor-checker-loading").show();
        $(".editor-checker-icon-ok").hide();
        $(".editor-checker-icon-error").hide();
        $(".editor-checker-board").hide();
        this.clearBoard();
    },
    success: function () {
        this.fitScrollbar();
        $(".ball-clip-rotate").show();
        $(".editor-checker-loading").hide();
        $(".editor-checker-icon-ok").fadeIn();
        $(".editor-checker-icon-error").hide();
        $(".editor-checker-board").hide();
        this.clearBoard();
    },
    error: function (obj) {
        this.fitScrollbar();
        $(".ball-clip-rotate").show();
        $(".editor-checker-loading").hide();
        $(".editor-checker-icon-ok").hide();
        $(".editor-checker-icon-error").text(obj.annotations.length).fadeIn();
        $(".editor-checker-board").hide();
        this.clearBoard();
        obj.annotations.forEach(function (annotation) {
            var row = parseInt(annotation.row, 10) + 1;
            var col = parseInt(annotation.column, 10) + 1;
            $(".editor-checker-board").append("<p style='cursor: pointer;' onclick='document.editor.gotoLine(" + row + ", " + col + ", true);'><u>" + annotation.text + ":" + row + ":" + col + "</u></p>");
        });
    },
    stop: function () {
        $(".ball-clip-rotate").hide();
        $(".editor-checker-board").hide();
    },
    clearBoard: function () {
        $(".editor-checker-board").html("");
    },
    goLine: function (annotation) {
        document.editor.gotoLine(annotation.row, annotation.column, true);
    },
    toggleBoard: function () {
        if (!$(".editor-checker-board").is(':animated')) {
            if ($(".editor-checker-board").is(':visible')) {
                $(".editor-checker-board").toggle('slide', {
                    direction: 'right'
                }, 500);
            } else {
                $(".editor-checker-board").toggle('slide', {
                    direction: 'right'
                }, 500);
            }
        }
    },
    fitScrollbar: function () {
        var editorHasScrollV = $(".ace_scrollbar-v").is(":visible");
        var editorHasScrollH = $(".ace_scrollbar-h").is(":visible");

        if (editorHasScrollV) {
            $(".ball-clip-rotate").css("right", "2.5em");
            $(".ball-clip-rotate.editor-checker-board").css({"right": "1.5em", "margin-right": "3.7em"});
        } else {
            $(".ball-clip-rotate").css("right", "1em");
            $(".ball-clip-rotate.editor-checker-board").css({"right": "0", "margin-right": "5.2em"});
        }

        if (editorHasScrollH) {
            $(".ball-clip-rotate").css("bottom", "2.5em");
            $(".editor-checker-board").css("bottom", "4.5em");
        } else {
            $(".ball-clip-rotate").css("bottom", "1em");
            $(".editor-checker-board").css("bottom", "3.5em");
        }

    }
};

/**
 * AdvancedModeManager to modify workbench views.
 */
var AdvancedModeManager = {
    getNodes: function () {
        return $(".dynatree-title");
    },
    getFilterLanguageExtensions: function () {
        return !!window["CONFIG_FILE_EXTENSIONS_TO_FILTER"] && CONFIG_FILE_EXTENSIONS_TO_FILTER ? CONFIG_FILE_EXTENSIONS_TO_FILTER: [];
    },
    getFilterValue: function () {
        return !!window["DEFAULT_FILTER_FILES"] && DEFAULT_FILTER_FILES ? DEFAULT_FILTER_FILES: false;
    },
    showFilteredNodes: function () {
        var _this = this;
        this.getNodes().each(function () {
            if (_this.getFilterLanguageExtensions().indexOf($(this).text().split('.').pop()) !== -1) {
                $(this).closest("li").show();
            }
        });
    },
    hideFilteredNodes: function () {
        var _this = this;
        this.getNodes().each(function () {
            var isFolder = $(this).closest("span").hasClass("dynatree-folder");
            if (!isFolder && _this.getFilterLanguageExtensions().indexOf($(this).text().split('.').pop()) !== -1) {
                $(this).closest("li").hide();
                //TODO: close files
            }
        });
    },
    apply: function () {
        if (this.getFilterValue() === false) {
            this.hideFilteredNodes();
        } else {
            this.showFilteredNodes();
        }
    }

};

var $scope = null;
var LanguageBindingsManifestManager = {
    apply: function (obj) {
        
        DescriptionInspector.angularFormatView.destroy();
        
        if (this.mayApply()) {
        
            var ang = obj.template;
            var ctl = obj.controller;
            var idSelector = obj.idSelector;

            if ($("#" + idSelector).length === 0) {
                $("#editorWrapper").append('<div id="'+idSelector+'"/>');
            }

            // Keep $scope.model saved
//            var cloneModel = Object.assign({}, $scope.model);
//            $scope = {};
//            $scope.model = cloneModel;

            // Clear current Binding visualization
//            $("#" + idSelector).empty().html(ang).append(
//                '<script>' +
//                'setTimeout(function() {' +
//                '  var $scope = angular.element(document.getElementById("appBody")).scope();' +
//                '  $scope.$apply(function () {' + ctl + '});' +
//                '}, 150);' +
//                '</script>'
//            );
//    
            
            setTimeout(function() {
                $("#" + idSelector).empty().html(ang);
                $scope.$apply(function () {
                    eval(ctl);
                });
                DescriptionInspector.tabs.angularCompileModelInspectorFormatView();
                DescriptionInspector.angularFormatView.formatTab.build();
                DescriptionInspector.angularFormatView.show();
            }, 150);
        
        }
    },
    clear: function () {
        DescriptionInspector.angularFormatView.destroy();
        $scope = angular.element(document.getElementById("appBody")).scope();
        setTimeout(function () {
            $scope.languageBindingsManifest = [];
            $scope.currentBinding = "";
            $scope.$apply();
        }, 150);
    },
    reloadPanel: function () {
        return !!$scope.$compile && $scope.$compile(angular.element("#bindingManagerPanel")[0])($scope);
    },
    load: function () {
        
        if (!window["$scope"]) {
            $scope = angular.element(document.getElementById("appBody")).scope();
        }
        
        if (this.mayApply()) {
            setTimeout(function () {
                var modelId = ModeManager.calculateModelIdFromExt(ModeManager.calculateExtFromFileUri(EditorManager.currentUri));
                var bindings = ModeManager.modelMap[modelId].bindings;
                
                $scope.model = jsyaml.safeLoad(document.editor.getValue());
                $scope.languageBindingsManifest = !!bindings && bindings.length > 0 ? bindings : [];
                $scope.$apply();
            }, 500);
        }
    },
    mayApply: function () {
        return !!window["$scope"] && !!$scope.languageBindingsManifest && 
                !!$scope.languageBindingsManifest.templateURL !== '' &&
                !!$scope.languageBindingsManifest.controllerURL !== '' &&
                !DescriptionInspector.existCurrentCtrlFile() &&
                !DescriptionInspector.existCurrentCtrlFile() &&
                !DescriptionInspector.existCurrentAngularFile();
    }
};