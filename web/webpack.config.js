const path = require('path');
const CopyPlugin = require("copy-webpack-plugin");
const Dotenv = require('dotenv-webpack');

// Get the name of the appropriate environment variable (`.env`) file for this build/run of the app
const dotenvFile = process.env.API_LOCATION ? `.env.${process.env.API_LOCATION}` : '.env';

module.exports = {
  plugins: [
    new CopyPlugin({
      patterns: [
        {
          from: "static_assets", to: "../",
          globOptions: {
            ignore: ["**/.DS_Store"],
          },
        },
      ],
    }),
    new Dotenv({ path: dotenvFile }),
  ],
  optimization: {
    usedExports: true
  },
  entry: {
    createRaid: path.resolve(__dirname, 'src', 'pages', 'createRaid.js'),
    viewRaid: path.resolve(__dirname, 'src', 'pages', 'viewRaid.js'),
    homepage: path.resolve(__dirname, 'src', 'pages', 'homepage.js'),
    profile: path.resolve(__dirname, 'src', 'pages', 'createProfile.js'),
    viewProfile: path.resolve(__dirname, 'src', 'pages', 'viewProfile.js'),
    editProfile: path.resolve(__dirname, 'src', 'pages', 'editProfile.js'),
    findRaid: path.resolve(__dirname, 'src', 'pages', 'findRaid.js')
  },
  output: {
    path: path.resolve(__dirname, 'build', 'assets'),
    filename: '[name].js',
    publicPath: '/assets/'
  },
  devServer: {
    static: {
      directory: path.join(__dirname, 'static_assets'),
    },
    port: 8000,
    client: {
      // overlay shows a full-screen overlay in the browser when there are js compiler errors or warnings
      overlay: true,
    },
  },
  module: {
    rules: [
      {
        test: /\.(png|jpg|jpeg|gif)$/i,
        type: 'asset/resource',
        generator: {
          filename: 'images/[name][ext]'
        }
      }
    ]
  }
}
