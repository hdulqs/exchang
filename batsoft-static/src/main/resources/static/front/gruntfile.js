module.exports = function (grunt) {
    /*
     *载入任务的插件
     * */
    grunt.loadNpmTasks('grunt-text-replace'); //替换
    grunt.loadNpmTasks('grunt-cmd-transport');  //CMD模块处理
    grunt.loadNpmTasks('grunt-cmd-concat');     //CMD模块合并
    grunt.loadNpmTasks('grunt-contrib-uglify'); //压缩
    grunt.loadNpmTasks('grunt-contrib-clean');  //清理
    grunt.loadNpmTasks('grunt-contrib-sass');   //SCSS
    grunt.loadNpmTasks('grunt-contrib-cssmin'); //css
    grunt.loadNpmTasks('grunt-contrib-copy');//copy

    /*
     * 项目配置
     * */
    grunt.initConfig({
        meta: {
            jsPath: 'src/script/',
            jsDist: 'dist/script/',
            scssPath: 'src/style/',
            cssPath: 'src/style/',
            cssDist: 'dist/style/',
            jsBuild: '.build/',
            cssCache: '.sass-cache'
        },
        /*构建时替换版本号*/
        replace: {
            version: {
                src: ['src/style/module/_variable.scss', '../../../config/constants.config.php'],
                overwrite: true,
                replacements: [{
                    from: /201(\d{5})(\d+)/,
                    to: function (a) {
                        if (a.substring(0, 8) == grunt.template.today('yyyymmdd')) {
                            a = a.substr(8) * 1 + 1;
                            return "<%= grunt.template.today('yyyymmdd')%>" + a;
                        } else {
                            return "<%= grunt.template.today('yyyymmdd')%>1";
                        }
                    }
                }]
            }
        },
        /*CMD模块转换*/
        transport: {
            options: {
                debug: false,
                paths: ['']
            },
            dev: {
                options: {
                    idleading: 'static/front/dist/script/' //添加ID前缀
                },
                files: [{
                    cwd: '<%= meta.jsPath %>',
                    src: ['!sea.js', '!loadjs.js', 'main.js', '!table**.js', 'module**.js', 'page**.js', '!jquery.js'],
                    filter: 'isFile',
                    expand: true,
                    dest: '<%= meta.jsBuild %>'
                }]
            }
        },
        /*CMD模块合并*/
        concat: {
            dist: {
                options: {
                    paths: '../<%= meta.jsPath %>',
                    include: 'relative'
                },
                files: [{
                    cwd: '<%= meta.jsBuild %>',
                    src: ['**', '!module**.js', '!plugins/**', '!demo/**'],
                    filter: 'isFile',
                    expand: true,
                    dest: '<%= meta.jsDist %>'
                }]
            }
        },
        //合并压缩CSS
        cssmin: {
            target: {
                files: {
                    'dist/style/app.css': [
                        'src/script/plugins/bootstrap/css/bootstrap.min.css',
                        'src/style/essentials.css',
                        'src/style/layout/layout.css',
                        'src/style/pagelevel/header-1.css',
                        'src/style/color_scheme/green.css',
                        'src/script/plugins/ladda/ladda-themeless.min.css',
                        'src/script/plugins/chosen/bootstrap-chosen.css',
                        'src/style/pack-realestate.css',
                        'src/style/site.css',
                        'src/script/plugins/sweetalert/sweetalert.css',
                        'src/script/plugins/jPagination/jquery.pagination.css',
                        'src/script/plugins/toastr/toastr.min.css',
                    ],'dist/style/book/app.css': [


                        /*'src/style/book/index.css',
                        'src/style/book/swiper.css',*/
                    ]
                }
            },
            dist: {
                options: {
                    report: 'gzip'
                },
                files: [
                    {
                        expand: true,
                        cwd: '<%= meta.cssDist %>',
                        src: ['**/*.css', '!*.min.css'],
                        dest: '<%= meta.cssDist %>',
                        ext: '.min.css'
                    }/*, {
                        expand: true,
                        cwd: '<%= meta.cssDist %>',
                        src: ['**!/book/!*.css', '!*.min.css'],
                        dest: '<%= meta.cssDist %>',
                        ext: '.min.css'
                    }*/
                ]
            }
        },
        /*JS压缩*/
        uglify: {
            dist: {
                options: {
                    compress: {
                        drop_console: true

                    },
                    mangle: false, //不混淆变量名
                    preserveComments: false //all:不删除注释, false:删除全部注释, some:保留@preserve @license @cc_on等注释
                },
                files: [{
                    expand: true,
                    cwd: '<%= meta.jsDist %>',
                    src: ['**/*.js', '!module**.js', /*'!sea.js',*/ '!plugins/**', '!demo/**'],
                    dest: '<%= meta.jsDist %>'
                }]
            }
        },
        //编译SCSS
        sass: {
            dist: {
                files: [{
                    expand: true,
                    cwd: '<%= meta.scssPath %>',
                    src: ['page/*.scss'],
                    dest: '<%= meta.cssDist %>',
                    ext: '.css'
                }],
                options: {
                    sourcemap: 'none',
                    style: 'compressed'
                }
            }
        },
        //copy
        copy: {
            main: {
                files: [

                    // makes all src relative to cwd
                    {expand: true, cwd: 'src/font-awesome/', src: '**', dest: 'dist/font-awesome/'},
                    {expand: true, cwd: 'src/fonts/', src: '**', dest: 'dist/fonts/'},
                    // {expand: true, cwd: 'src/style/', src: '**', dest: 'dist/style/'},
                    {expand: true, cwd: 'src/img/', src: '**', dest: 'dist/img/'},
                    {
                        expand: true,
                        cwd: '<%= meta.jsPath %>',
                        src: ['**', '!module**.js', '!page**.js', '!module/**'],
                        dest: '<%= meta.jsDist %>'
                    },

                ],
            },
        },
        /*清除临时文件*/
        clean: ['<%= meta.jsBuild %>**', '<%= meta.cssCache %>**']
    });

    //注册任务
    grunt.registerTask('default', ['copy', 'transport', 'concat', 'uglify', 'cssmin'/*,'sass'*/, 'clean']);
};