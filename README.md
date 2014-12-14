Programmer Execuses
===================

Simple client which uses [pe-api](https://github.com/yelinaung/pe-api). 

It's now available on Play Store

<a href="https://play.google.com/store/apps/details?id=com.yelinaung.programmerexcuses">
  <img alt="Get it on Google Play" src="https://developer.android.com/images/brand/en_generic_rgb_wo_60.png" />
</a>


![Screenshot](https://raw.githubusercontent.com/yelinaung/ProgrammerExcuses/master/screenshot.png)

It's using 
- [SwipeRefreshLayout](https://developer.android.com/reference/android/support/v4/widget/SwipeRefreshLayout.html) for swipe-down-to-refresh layout.
- [ButterKinife](https://jakewharton.github.io/butterknife) for view injections.
- [Retrofit](https://github.com/square/retrofit) for REST client.

TODO
----
- ~~"Share"~~
- Daily Notifications

Required Permissions
--------------------
`android.permission.INTERNET` - Need to access to API to fetch the message
`android.permission.ACCESS_NETWORK_STATE` - Need to access whether the host device is offline/not connected to wifi

Contributing
------------

 1. Fork it
 2. Create your feature branch (`git checkout -b my-new-feature`)
 3. Commit your changes (`git commit -am 'Added some feature'`)
 4. Push to the branch (`git push origin my-new-feature`)
 5. Create new Pull Request

License
--------

    Copyright 2014 Ye Lin Aung

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
