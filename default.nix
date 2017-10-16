{ pkgs ? import <nixpkgs> {} }:

with pkgs;

stdenv.mkDerivation {
  name = "baoqu-front";
  src = ./.;

  buildInputs = [
    openjdk
    leiningen
    nodejs-6_x
    yarn
  ];
}
