'use strict';

var gulp = require('gulp'),
    clean = require('gulp-clean'),
    watch = require('gulp-watch'),
    plumber = require('gulp-plumber'),
    autoprefixer = require("gulp-autoprefixer"),
    scss = require('gulp-sass'),
    concat = require('gulp-concat'),
    jade = require('gulp-jade'),
    browserSync = require('browser-sync').create(),
    paths = {
      app: 'src/app/**/*.js',
      globals: 'js/**/*.js',
      dist: '../resources/public/',
      //dist: 'dist/',
      js: 'js/**/*.*',
      images: 'images/**/*.*',
      fonts: 'fonts/**/*.*',
      icons: 'icons/**/*.*',
      scss: 'styles/**/*.scss',
      vendor: 'vendor/**/*.*',
      allTemplates: 'templates/**/*'
    };

gulp.task('clean', function() {
  gulp.src(paths.dist).pipe(clean());
});

gulp.task('js', function() {
  gulp.src(paths.js)
    .pipe(gulp.dest(paths.dist + 'js/'));
});

gulp.task('fonts', function() {
  gulp.src(paths.fonts)
    .pipe(gulp.dest(paths.dist + 'fonts/'));
});

gulp.task('images', function() {
  gulp.src(paths.images)
  .pipe(gulp.dest(paths.dist + 'images/'));
});

gulp.task('icons', function() {
  gulp.src(paths.icons)
  .pipe(gulp.dest(paths.dist + 'icons/'));
});

gulp.task('scss', function() {
  gulp.src('styles/main.scss')
    .pipe(scss({ style: 'expanded' }))
    .pipe(autoprefixer({
        browsers: ['last 2 versions'],
        cascade: false
    }))
    .pipe(gulp.dest(paths.dist + 'styles/'))
});

gulp.task('vendor', function() {
  gulp.src(paths.vendor)
    .pipe(gulp.dest(paths.dist + 'vendor/'));
});

gulp.task('templates', function() {
  gulp.src(paths.allTemplates)
    .pipe(plumber())
    .pipe(jade({ pretty: true }))
    .pipe(gulp.dest(paths.dist));
});

gulp.task('dist', ['images', 'js', 'fonts', 'icons', 'templates', 'vendor', 'scss']);

gulp.task('browser-sync', function() {
  browserSync.init({
    server: {
      baseDir: paths.dist
    },
    notify: false,
    open: false
  });
});

gulp.task('default', ['dist', 'browser-sync'], function() {
  gulp.watch(paths.scss, ['scss', browserSync.reload]);
  gulp.watch(paths.js, ['js', browserSync.reload]);
  gulp.watch(paths.fonts, ['fonts', browserSync.reload]);
  gulp.watch(paths.icons, ['icons', browserSync.reload]);
  gulp.watch(paths.images, ['images', browserSync.reload]);
  gulp.watch(paths.allTemplates, ['templates', browserSync.reload]);
});
